/**
 * Copyright (C) Intersect 2010.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FinishPage.java 202 2010-07-28 22:54:45Z georgina $
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

/**
 * @version $Rev: 202 $
 * 
 */
public class FinishPage extends WizardPage implements FinishablePage
{
    private static final Logger LOG = Logger.getLogger(FinishPage.class);
    
    private Label processedRowsLabel;

    protected FinishPage(IWizard wizard)
    {
        super("finish page");
        this.setWizard(wizard);
        LOG.info("created FinishPage");
    }

    public void createControl(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        composite.setLayout(gridLayout);
        this.processedRowsLabel = new Label(composite, SWT.NONE);
        this.processedRowsLabel.setText("?processed rows go here??processed rows go here??processed rows go here?");
        new Label(composite, SWT.NONE).setText("Click Finish to exit");
        this.setControl(composite);
        Attribution.addAttribution(composite, getShell());
    }

    @Override
    public boolean canFlipToNextPage()
    {
        return false;
    }

    @Override
    public IWizardPage getPreviousPage()
    {
        return null;
    }

    public void setNumProcessedRows(int numProcessedRows)
    {
        this.processedRowsLabel.setText("" + numProcessedRows + " row(s) successfully processed");
    }
}
