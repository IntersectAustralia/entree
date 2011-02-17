/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: EntreeWizardDialog.java 63 2010-06-23 04:18:22Z ryan $
 */
package au.org.intersect.entreeui.ui.wizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @version $Rev: 63 $
 * 
 */
public class EntreeWizardDialog extends WizardDialog
{
    private static final int WINDOW_HEIGHT = 480;
    private static final int WINDOW_WIDTH = 728;

    public EntreeWizardDialog(Shell parentShell, IWizard newWizard)
    {
        super(parentShell, newWizard);
    }

    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
        newShell.setMinimumSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        newShell.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

}
