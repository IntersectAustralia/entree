/**
 * Copyright (C) Intersect 2010.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: EntreeApplication.java 244 2010-09-10 03:53:57Z ryan $
 */
package au.org.intersect.entreeui.ui;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import au.org.intersect.entreecore.worker.EntreeWorker;
import au.org.intersect.entreecore.worker.ProgressListener;
import au.org.intersect.entreeui.ui.wizard.EntreeWizard;
import au.org.intersect.entreeui.ui.wizard.EntreeWizardDialog;
import au.org.intersect.entreeui.ui.wizard.FailureScreenFactory;
import au.org.intersect.entreeui.worker.ExtractOnlyWorker;

/**
 * The entry-point for the Entree application.
 * 
 * @version $Rev: 244 $
 * 
 */
public class EntreeApplication extends ApplicationWindow
{

    private final ProgressBarListener csvProgressBarListener = new ProgressBarListener();
    private final ProgressBarListener ftpProgressBarListener = new ProgressBarListener();

    private final EntreeWorker entreeWorker;
    private final ExtractOnlyWorker extractWorker;

    private FailureScreenFactory failureScreenFactory;

    public EntreeApplication(EntreeWorker entreeWorker, ExtractOnlyWorker extractWorker,
            FailureScreenFactory failureScreenFactory)
    {
        super(null);
        this.entreeWorker = entreeWorker;
        this.extractWorker = extractWorker;

        this.failureScreenFactory = failureScreenFactory;
        setIcon();
    }

    private void setIcon()
    {
        Window.setDefaultImage(new Image(Display.getDefault(), ClassLoader
                .getSystemResourceAsStream("icon_entree_64x64.png")));
    }

    /**
     * Create the wizard dialog
     * 
     * @param parent
     *            the composite where to create the wizard
     */
    @Override
    protected Control createContents(Composite parent)
    {
        EntreeWizard wizard = new EntreeWizard(entreeWorker, extractWorker, csvProgressBarListener,
                ftpProgressBarListener, failureScreenFactory);
        WizardDialog dialog = new EntreeWizardDialog(getShell(), wizard);
        dialog.setBlockOnOpen(true);
        dialog.open();
        return parent;

    }

    public ProgressListener getCsvProgressListener()
    {
        return this.csvProgressBarListener;
    }

    public ProgressListener getFtpProgressListener()
    {
        return this.ftpProgressBarListener;
    }

}
