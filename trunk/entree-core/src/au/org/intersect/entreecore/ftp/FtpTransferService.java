/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FtpTransferService.java 202 2010-07-28 22:54:45Z georgina $
 */
package au.org.intersect.entreecore.ftp;

import java.io.File;

import org.apache.log4j.Logger;

import au.org.intersect.entreecore.filesender.FileSender;
import au.org.intersect.entreecore.filesender.FileSendingException;
import au.org.intersect.entreecore.worker.ProgressListener;
import au.org.intersect.filetransfer.TransferException;
import au.org.intersect.filetransfer.TransferService;
import au.org.intersect.filetransfer.TransferStatus;

/**
 * @version $Rev: 202 $
 * 
 */
public class FtpTransferService implements FileSender
{
    /**
     * Delay between updating the progressListener
     */
    private static final int PROGRESS_INTERVAL_DELAY = 500;
    private static final Logger LOG = Logger.getLogger(FtpTransferService.class);
    private static final int MAX_FTP_PROGRESS = 10000;

    private final TransferService transferService;
    private final String destination;

    private TransferException e;

    public FtpTransferService(TransferService transferService, String destination)
    {
        this.transferService = transferService;
        this.destination = destination;
    }

    public void uploadFile(String filePath, ProgressListener progressListener) throws FileSendingException
    {
        this.e = null;
        Thread thread = startTransferInNewThread(filePath);

        progressListener.setMaxProgress(MAX_FTP_PROGRESS);
        updateStatusUntilComplete(progressListener);
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            LOG.warn(e);
        }
        if (this.e != null)
        {
            LOG.error("caught: ",  e);
            throw new FileSendingException(e);
        }
    }

    private Thread startTransferInNewThread(final String fileToUpload)
    {
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    transferService.transfer(new File(fileToUpload), destination);
                }
                catch (TransferException e)
                {
                    FtpTransferService.this.e = e;
                }
                TransferStatus status = transferService.getTransferStatus();
                if (status != null && status.faliure() != null)
                {
                    FtpTransferService.this.e = status.faliure();
                }
            }
        });
        thread.start();
        return thread;
    }

    private void updateStatusUntilComplete(ProgressListener progressListener)
    {
        TransferStatus status;
        do
        {
            status = transferService.getTransferStatus();
            // If the transfer hasn't started yet, the status will be null
            while (status == null)
            {
                status = transferService.getTransferStatus();
            }
            try
            {
                Thread.sleep(PROGRESS_INTERVAL_DELAY);
            }
            catch (InterruptedException e)
            {
                // ignore
            }
            updateFtpStatus(progressListener, status);

        } while (!status.isCompleted() && !status.isFailed());
        if (status.isFailed())
        {
            LOG.error(status.faliure());
            this.e = status.faliure();
        }

    }

    private void updateFtpStatus(ProgressListener progressListener, TransferStatus transferStatus)
    {
        double current = transferStatus.getTotalTransferred();
        double total = transferStatus.getTotalToTransfer();
        double proportion = current / total;

        int scaledProportion = (int) (MAX_FTP_PROGRESS * proportion);
        progressListener.updateProgress(scaledProportion);
    }

}
