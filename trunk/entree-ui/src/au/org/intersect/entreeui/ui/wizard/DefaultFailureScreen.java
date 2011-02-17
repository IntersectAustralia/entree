/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: DefaultFailureScreen.java 202 2010-07-28 22:54:45Z georgina $
 */
package au.org.intersect.entreeui.ui.wizard;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import au.org.intersect.entreecore.worker.WorkFailedException;

/**
 * @version $Rev: 202 $
 * 
 */
public class DefaultFailureScreen extends WizardPage implements FinishablePage
{
    private static final Logger LOG = Logger.getLogger(DefaultFailureScreen.class);

    private final WorkFailedException exception;

    public DefaultFailureScreen(WorkFailedException e, IWizard w)
    {
        super("Default Failure Screen");
        this.exception = e;

        this.setWizard(w);
    }

    /**
     * @param t
     * @param w
     */
    public DefaultFailureScreen(Throwable t, IWizard w)
    {
        super("Default Failure Screen");
        if (t instanceof WorkFailedException)
        {
            this.exception = (WorkFailedException) t;
        }
        else
        {
            this.exception = null;
        }
        LOG.error("unexpected", t);
        this.setWizard(w);
    }

    public void createControl(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        Label l = new Label(composite, SWT.NONE);
        String message = null;
        if (exception != null)
        {
            message = exception.getDisplayMessage();
        }
        if (message == null)
        {
            message = "An unknown problem persists.";
        }
        l.setText(message);

        this.setControl(composite);
        
        Attribution.addAttribution(composite, getShell());
    }

    @Override
    public IWizardPage getPreviousPage()
    {
        return null;
    }

}
