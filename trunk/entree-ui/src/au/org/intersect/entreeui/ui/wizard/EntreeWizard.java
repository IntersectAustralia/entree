/**
 * Copyright (C) Intersect 2010.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: EntreeWizard.java 244 2010-09-10 03:53:57Z ryan $
 */
package au.org.intersect.entreeui.ui.wizard;

import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import au.org.intersect.entreecore.worker.EntreeWorker;
import au.org.intersect.entreeui.ui.ProgressBarListener;
import au.org.intersect.entreeui.worker.ExtractOnlyWorker;

/**
 * @version $Rev: 244 $
 * 
 */
public class EntreeWizard extends Wizard
{
    private static final String CANCEL_WHILE_IN_PROGRESS_MESSAGE = "Are you sure you want to cancel your operation"
            + " and exit the application?";
    private static final String CANCEL_MESSAGE = "Are you sure you want to exit the application?";

    private static final Logger LOG = Logger.getLogger(EntreeWizard.class);

    private final FailureScreenFactory failureScreenFactory;
    private final EntreeWorker entreeWorker;
    private final ExtractOnlyWorker extractWorker;
    private FinishPage finishPage;
    private ProgressPage progressPage;
    private ProgressBarListener ftpProgressListener;
    private ProgressBarListener csvProgressListener;

    public EntreeWizard(EntreeWorker entreeWorker, ExtractOnlyWorker extractWorker,
            ProgressBarListener csvProgressListener, ProgressBarListener ftpProgressListener,
            FailureScreenFactory failureScreenFactory)
    {
        this.setWindowTitle("Entree Wizard");
        this.entreeWorker = entreeWorker;
        this.extractWorker = extractWorker;

        this.csvProgressListener = csvProgressListener;
        this.ftpProgressListener = ftpProgressListener;

        this.failureScreenFactory = failureScreenFactory;

        setupBanner();
    }

    private void setupBanner()
    {
        URL bannerImageUrl = ClassLoader.getSystemResource("EntreeBanner.png");
        this.setDefaultPageImageDescriptor(ImageDescriptor.createFromURL(bannerImageUrl));
    }

    @Override
    public boolean performFinish()
    {
        LOG.info("performing finish");
        return true;
    }

    @Override
    public boolean performCancel()
    {
        boolean isProgressPage = this.getContainer().getCurrentPage() == progressPage;
        boolean isProcessing = isProgressPage && progressPage.hasStarted() && !progressPage.hasFinished();
        String cancelMessage = isProcessing ? CANCEL_WHILE_IN_PROGRESS_MESSAGE : CANCEL_MESSAGE;
        boolean confirmCancel = MessageDialog.openConfirm(getShell(), "Confirmation", cancelMessage);
        if (!confirmCancel)
        {
            return false;
        }
        if (!isProgressPage)
        {
            return true;
        }
        else
        {
            // there's 4 possible outcomes here:
            // 1. we hadn't started doing anything yet
            if (!progressPage.hasStarted())
            {
                // just exit
                return true;
            }
            // 2. we'd already finished so it was definitely too late to cancel
            if (progressPage.hasFinished())
            {
                // just exit
                return true;
            }
            
            boolean wasCancelled = progressPage.doCancel();

            if (wasCancelled)
            {
                // 3. we'd started and have been able to cancel
                // advise them that the operation was cancelled and then exit
                MessageDialog.openInformation(getShell(), "Operation Cancelled",
                        "The operation was cancelled and no data has been transferred.");
                return true;
            }
            else
            {
                // 4. we'd started and it was too late to cancel
                // advise them that it was too late and don't exit the application
                MessageDialog.openInformation(getShell(), "Too Late to Cancel",
                        "The operation could not be cancelled as the transfer was already underway or completed.");
                return false;
            }
        }
    }

    @Override
    public void addPages()
    {
        EntreeWorker entreeWorker = this.entreeWorker;
        ExtractOnlyWorker extractWorker = this.extractWorker;
        this.progressPage = new ProgressPage(this, entreeWorker, extractWorker, csvProgressListener,
                ftpProgressListener, failureScreenFactory);

        this.finishPage = new FinishPage(this);
        this.addPage(progressPage);
        this.addPage(finishPage);
    }

    @Override
    public boolean canFinish()
    {
        IWizardPage currentPage = this.getContainer().getCurrentPage();
        return currentPage != null && currentPage instanceof FinishablePage;
    }

    public void notifyNumProcessedRows(int numProcessedRows)
    {
        this.finishPage.setNumProcessedRows(numProcessedRows);
    }

}
