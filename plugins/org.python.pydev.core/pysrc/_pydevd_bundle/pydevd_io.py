from _pydevd_bundle import pydevd_constants

IS_PY3K = pydevd_constants.IS_PY3K

class IORedirector:
    '''This class works to redirect the write function to many streams
    '''

    def __init__(self, original, new_redirect, wrap_buffer=False):
        self._redirect_to = (original, new_redirect)
        if wrap_buffer:
            self.buffer = IORedirector(original.buffer, new_redirect.buffer, False)

    def write(self, s):
        # The original one may fail if the user tries to write a number to the stream, etc.
        self._redirect_to[0].write(s)
        try:
            # This one should not fail.
            self._redirect_to[1].write(s)
        except:
            pass

    def isatty(self):
        return self._redirect_to[0].isatty()

    def flush(self):
        for r in self._redirect_to:
            r.flush()

    def __getattr__(self, name):
        for r in self._redirect_to:
            if hasattr(r, name):
                return getattr(r, name)
        raise AttributeError(name)

class IOBuf:
    '''This class works as a replacement for stdio and stderr.
    It is a buffer and when its contents are requested, it will erase what
    it has so far so that the next return will not return the same contents again.
    '''
    def __init__(self):
        self.buflist = []
        import os
        self.encoding = os.environ.get('PYTHONIOENCODING', 'utf-8')

    def getvalue(self):
        b = self.buflist
        self.buflist = []  # clear it
        return ''.join(b)  # bytes on py2, str on py3.
    
    def write(self, s):
        if not IS_PY3K:
            if isinstance(s, unicode):
                # can't use 'errors' as kwargs in py 2.6
                s = s.encode(self.encoding, 'replace')
        else:
            if isinstance(s, bytes):
                s = s.decode(self.encoding, errors='replace')
        self.buflist.append(s)

    def isatty(self):
        return False

    def flush(self):
        pass

    def empty(self):
        return len(self.buflist) == 0

class _RedirectionsHolder:
    _stack_stdout = []
    _stack_stderr = []


def start_redirect(keep_original_redirection=False, std='stdout'):
    '''
    @param std: 'stdout', 'stderr', or 'both'
    '''
    import sys
    buf = IOBuf()

    if std == 'both':
        config_stds = ['stdout', 'stderr']
    else:
        config_stds = [std]

    for std in config_stds:
        original = getattr(sys, std)
        stack = getattr(_RedirectionsHolder, '_stack_%s' % std)
        stack.append(original)

        if keep_original_redirection:
            setattr(sys, std, IORedirector(getattr(sys, std), buf))
        else:
            setattr(sys, std, buf)
    return buf


def end_redirect(std='stdout'):
    import sys
    if std == 'both':
        config_stds = ['stdout', 'stderr']
    else:
        config_stds = [std]
    for std in config_stds:
        stack = getattr(_RedirectionsHolder, '_stack_%s' % std)
        setattr(sys, std, stack.pop())

