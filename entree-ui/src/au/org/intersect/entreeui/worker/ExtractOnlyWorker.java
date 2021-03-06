/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: ExtractOnlyWorker.java 244 2010-09-10 03:53:57Z ryan $
 */
package au.org.intersect.entreeui.worker;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import au.org.intersect.entreecore.csv.CsvGenerator;
import au.org.intersect.entreecore.csv.CsvWriter;
import au.org.intersect.entreecore.datasource.db.NotifyingRowCallbackHandler;
import au.org.intersect.entreecore.datasource.db.Querier;
import au.org.intersect.entreecore.worker.Cancellable;
import au.org.intersect.entreecore.worker.EntreeWorker;
import au.org.intersect.entreecore.worker.OperationCancelledException;
import au.org.intersect.entreecore.worker.ProgressListener;
import au.org.intersect.entreecore.worker.WorkFailedException;
import au.org.intersect.entreeui.util.DateUtil;
import au.org.intersect.entreeui.util.FilesystemUtil;

/**
 * A worker that only extracts the file and does not attempt to upload it in any way
 * 
 * @version $Rev: 244 $
 * 
 */
public class ExtractOnlyWorker implements EntreeWorker, Cancellable
{
    private static final Logger LOG = Logger.getLogger(ExtractOnlyWorker.class);

    private final Querier querier;
    private final NotifyingRowCallbackHandler notifyingRowCallbackHandler;
    private final String filePath;
    private final CsvWriter csvWriter;
    private final CsvGenerator csvGenerator;
    private final FilesystemUtil filesystemUtil;
    private boolean cancelRequested;

    public ExtractOnlyWorker(Querier querier, NotifyingRowCallbackHandler notifyingRowCallbackHandler,
            CsvWriter csvWriter, FilesystemUtil filesystemUtil, CsvGenerator csvGenerator, String filePath)
    {
        this.querier = querier;
        this.notifyingRowCallbackHandler = notifyingRowCallbackHandler;
        this.csvWriter = csvWriter;
        this.filePath = substituteCurrentDate(filePath);
        this.filesystemUtil = filesystemUtil;
        this.csvGenerator = csvGenerator;
    }

    private String substituteCurrentDate(String filePath)
    {
        return filePath.replaceAll("CURRENTDATE", DateUtil.formatToday());
    }

    public int doWork(ProgressListener csvProgressListener, ProgressListener ftpProgressListener)
        throws WorkFailedException
    {
        this.cancelRequested = false; // reset the worker so it can be called again
        notifyingRowCallbackHandler.resetCancel();

        int numProcessedRows = 0;
        try
        {
            checkIfShouldCancel();
            filesystemUtil.ensureDirsExist(filePath);
            csvWriter.open(new File(filePath));
            csvWriter.writeLine(csvGenerator.getHeaderRow());

            checkIfShouldCancel();
            notifyingRowCallbackHandler.setListener(csvProgressListener);
            querier.processRows(notifyingRowCallbackHandler);
            numProcessedRows = notifyingRowCallbackHandler.getNumProcessedRows();

            checkIfShouldCancel();
        }
        catch (IOException e)
        {
            throw new WorkFailedException("Could not open " + filePath, e);
        }
        finally
        {
            csvWriter.close();
        }
        checkIfShouldCancel();

        return numProcessedRows;
    }

    private void checkIfShouldCancel()
    {
        if (this.cancelRequested)
        {
            LOG.info("Cancel requested on UI worker, throwing OperationCancelledException");
            throw new OperationCancelledException();
        }
    }

    public void requestCancel()
    {
        this.cancelRequested = true;
        notifyingRowCallbackHandler.requestCancel();
    }

    public String getFilePath()
    {
        return this.filePath;
    }

}
