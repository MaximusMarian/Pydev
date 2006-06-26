/*
 * License: Common Public License v1.0
 * Created on Oct 25, 2004
 * 
 * @author Fabio Zadrozny
 */
package org.python.pydev.runners;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.python.pydev.core.Tuple;
import org.python.pydev.plugin.PydevPlugin;

/**
 * 
 * This class has some useful methods for running a python script.
 * 
 * It is not as complete as the PythonRunner from the debug, as it doesn't register the process in the console, but it can be quite useful
 * for other runs.
 * 
 * 
 * Interesting reading for http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html  -  
 * Navigate yourself around pitfalls related to the Runtime.exec() method
 * 
 * 
 * @author Fabio Zadrozny
 */
public class SimplePythonRunner extends SimpleRunner {

    
    /**
     * Execute the script specified with the interpreter for a given project 
     * 
     * @param script the script we will execute
     * @param args the arguments to pass to the script
     * @param workingDir the working directory
     * @param project the project that is associated to this run
     * 
     * @return a string with the output of the process (stdout)
     */
    public Tuple<String,String> runAndGetOutput(String script, String[] args, File workingDir, IProject project) {
        String executionString = makeExecutableCommandStr(script, args);
        return runAndGetOutput(executionString, workingDir, project);
    }

    /**
     * @param script the script to run
     * @param args the arguments to be passed to the script
     * @return the string with the command to run the passed script with jython
     */
    public static String makeExecutableCommandStr(String script, String[] args) {
        String[] s = new String[]{
            PydevPlugin.getPythonInterpreterManager().getDefaultInterpreter() , 
            "-u" ,
            script ,
        };

        return getCommandLineAsString(s, args);
    }

    /**
     * Execute the string and format for windows if we have spaces...
     * 
     * The interpreter can be specified.
     * 
     * @param interpreter the interpreter we want to use for executing
     * @param script the python script to execute
     * @param args the arguments to the scripe
     * @param workingDir the directory where the script should be executed
     * 
     * @return the stdout of the run (if any)
     */
    public Tuple<String,String>  runAndGetOutputWithInterpreter(String interpreter, String script, String[] args, File workingDir, IProject project, IProgressMonitor monitor) {
        monitor.setTaskName("Mounting executable string...");
        monitor.worked(5);
        
        File file = new File(script);
        if(file.exists() == false){
            throw new RuntimeException("The script passed for execution ("+script+") does not exist.");
        }
        
        String[] s = new String[]{
            interpreter, 
            "-u" ,
            script ,
        };

        monitor.worked(1);
        return runAndGetOutput(getCommandLineAsString(s,args), workingDir, project, monitor);
    }




    
}