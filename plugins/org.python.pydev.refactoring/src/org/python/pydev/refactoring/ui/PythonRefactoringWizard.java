/* 
 * Copyright (C) 2006, 2007  Dennis Hunziker, Ueli Kistler
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 * 
 */

package org.python.pydev.refactoring.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.python.pydev.refactoring.PepticPlugin;
import org.python.pydev.refactoring.core.AbstractPythonRefactoring;
import org.python.pydev.refactoring.messages.Messages;

public class PythonRefactoringWizard extends RefactoringWizard {
    protected AbstractPythonRefactoring refactoring;
    private ITextEditor targetEditor;

    public PythonRefactoringWizard(AbstractPythonRefactoring refactoring, ITextEditor targetEditor) {
        super(refactoring, WIZARD_BASED_USER_INTERFACE);
        ImageDescriptor wizardImg = PepticPlugin.imageDescriptorFromPlugin(PepticPlugin.PLUGIN_ID, Messages.imagePath + Messages.imgLogo);

        this.targetEditor = targetEditor;
        this.refactoring = refactoring;
        this.setDefaultPageImageDescriptor(wizardImg);
        this.setWindowTitle(refactoring.getName());
    }

    @Override
    protected void addUserInputPages() {
        this.getShell().setMinimumSize(640, 480);
        for(IWizardPage page:refactoring.getPages()){
            addPage(page);
        }
    }

    public void run() {
        try{
            RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(this);

            op.run(getShell(), refactoring.getName());
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Looks for an usable shell
     */
    public Shell getShell() {
        return targetEditor != null ? targetEditor.getSite().getShell() : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }
}