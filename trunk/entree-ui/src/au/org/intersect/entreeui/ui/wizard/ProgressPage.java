/**
 * Copyright (C) Intersect 2010.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: ProgressPage.java 244 2010-09-10 03:53:57Z ryan $
 */
package au.org.intersect.entreeui.ui.wizard;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import au.org.intersect.entreecore.worker.EntreeWorker;
import au.org.intersect.entreecore.worker.OperationCancelledException;
import au.org.intersect.entreecore.worker.ProgressListener;
import au.org.intersect.entreeui.ui.ProgressBarListener;
import au.org.intersect.entreeui.ui.TestButtonFactory;
import au.org.intersect.entreeui.worker.ExtractOnlyWorker;

/**
 * @version $Rev: 244 $
 * 
 */
public class ProgressPage extends WizardPage
{
    private static final int ONE_MINUTE = 1000 * 60;
    private static final Logger LOG = Logger.getLogger(ProgressPage.class);

    private final EntreeWorker entreeWorker;
    private final ExtractOnlyWorker extractWorker;
    private final EntreeWizard entreeWizard;
    private final ProgressBarListener csvProgressListener;
    private final ProgressBarListener ftpProgressListener;
    private final FailureScreenFactory failureScreenFactory;
    private boolean finishedProcessing;
    private boolean startedProcessing;
    private Throwable error;
    private ProgressBar csvProgress;
    private ProgressBar ftpProgress;
    private Label label;
    private Thread workerThread;
    private boolean wasCancelled;

    protected ProgressPage(EntreeWizard entreeWizard, EntreeWorker entreeWorker, ExtractOnlyWorker extractWorker,
            ProgressBarListener csvProgressListener, ProgressBarListener ftpProgressListener,
            FailureScreenFactory failureScreenFactory)
    {
        super("progress page");
        this.setWizard(entreeWizard);
        this.entreeWorker = entreeWorker;
        this.extractWorker = extractWorker;

        this.csvProgressListener = csvProgressListener;
        this.ftpProgressListener = ftpProgressListener;
        this.failureScreenFactory = failureScreenFactory;
        this.entreeWizard = entreeWizard;
        LOG.info("created ProgressPage");
    }

    public void createControl(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        composite.setLayout(gridLayout);
        this.label = new Label(composite, SWT.NONE);
        this.label.setText("Click start to begin.");
        this.csvProgress = new ProgressBar(composite, SWT.SMOOTH);
        Label csvLabel = new Label(composite, SWT.NONE);
        csvLabel.setText("CSV creation progress");
        this.ftpProgress = new ProgressBar(composite, SWT.NULL);
        Label ftpLabel = new Label(composite, SWT.NONE);
        ftpLabel.setText("Upload progress");
        csvProgress.setToolTipText("Progress of CSV creation");
        ftpProgress.setToolTipText("Progress of file uploading");
        this.setControl(composite);
        final Button button = new Button(composite, SWT.NONE);
        button.setText("Start");
        
        final Button testButton = TestButtonFactory.createTestButton(composite, extractWorker);

        button.addSelectionListener(new SelectionListener()
        {
            public void widgetSelected(SelectionEvent event)
            {
                button.setEnabled(false);
                testButton.setEnabled(false);
                ProgressPage.this.label.setText("Processing...");

                ProgressPage.this.startedProcessing = true;
                // disable previous page button when starting processing
                ProgressPage.this.getWizard().getContainer().updateButtons();
                extractAndSend();
            }

            public void widgetDefaultSelected(SelectionEvent event)
            {
                this.widgetSelected(event);
            }
        });
        
        this.csvProgressListener.setProgressBar(this.csvProgress);
        this.csvProgressListener.setDisplay(this.getShell().getDisplay());
        this.ftpProgressListener.setProgressBar(this.ftpProgress);
        this.ftpProgressListener.setDisplay(this.getShell().getDisplay());

        Attribution.addAttribution(composite, getShell());
    }

    @Override
    public boolean canFlipToNextPage()
    {
        return finishedProcessing;
    }

    @Override
    public IWizardPage getPreviousPage()
    {
        return startedProcessing ? null : super.getPreviousPage();
    }

    @Override
    public IWizardPage getNextPage()
    {
        return this.error == null ? super.getNextPage() : failureScreenFactory.getFailureScreen(this.error,
                this.getWizard());
    }

    private void extractAndSend()
    {
        final ProgressListener csvProgressListener = this.csvProgressListener;
        final ProgressListener ftpProgressListener = this.ftpProgressListener;
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                int numProcessedRows = 0;
                try
                {
                    numProcessedRows = entreeWorker.doWork(csvProgressListener, ftpProgressListener);
                }
                catch (OperationCancelledException e)
                {
                    // just return and don't update the UI
                    ProgressPage.this.wasCancelled = true;
                    return;
                }
                catch (Throwable t)
                {
                    LOG.info("Entree Worker threw an exception", t);
                    ProgressPage.this.error = t;
                }
                ProgressPage.this.wasCancelled = false;
                LOG.info("About to update UI after processing");
                updateUiAfterProcessingFinished(numProcessedRows);
            }
        }, "worker thread");
        workerThread.start();
    }

    private void updateUiAfterProcessingFinished(final int numProcessedRows)
    {
        ProgressPage.this.finishedProcessing = true;
        getShell().getDisplay().asyncExec(new Runnable()
        {
            public void run()
            {
                ProgressPage.this.entreeWizard.getContainer().updateButtons();
                ProgressPage.this.entreeWizard.notifyNumProcessedRows(numProcessedRows);

                String finishText = ProgressPage.this.error == null ? "Done" : "An error occurred.";
                ProgressPage.this.label.setText(finishText);
            }
        });
    }

    public boolean doCancel()
    {
        this.label.setText("Cancelling...");
        entreeWorker.requestCancel();
        LOG.info("Requested cancel on entree worker, waiting for worker thread to finish");
        if (workerThread == null || !workerThread.isAlive())
        {
            return this.wasCancelled;
        }
        // the thread is alive, so wait for it to die
        try
        {
            workerThread.join(ONE_MINUTE); // allow 1 minute for it to die, otherwise just exit anyway
        }
        catch (InterruptedException e)
        {
            // ignore and continue on
        }
        return this.wasCancelled;
    }

    public boolean hasStarted()
    {
        return this.startedProcessing;
    }

    public boolean hasFinished()
    {
        return this.finishedProcessing;
    }
    
}
