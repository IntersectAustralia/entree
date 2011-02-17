/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: UiWorkerTest.java 229 2010-08-11 23:23:33Z georgina $
 */
package au.org.intersect.entreeui.worker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.entreecore.csv.CsvGenerator;
import au.org.intersect.entreecore.csv.CsvWriter;
import au.org.intersect.entreecore.datasource.db.NotifyingRowCallbackHandler;
import au.org.intersect.entreecore.datasource.db.Querier;
import au.org.intersect.entreecore.filesender.FileSender;
import au.org.intersect.entreecore.worker.ProgressListener;
import au.org.intersect.entreecore.worker.WorkFailedException;
import au.org.intersect.entreeui.util.FilesystemUtil;

/**
 * @version $Rev: 229 $
 * 
 */
public class UiWorkerTest
{
    @Mock
    private Querier querier;
    @Mock
    private NotifyingRowCallbackHandler notifyingRowCallbackHandler;

    @Mock
    private FileSender fileSender;
    
    @Mock
    private FilesystemUtil filesystemUtil;

    @Mock
    private CsvWriter csvWriter;
    
    @Mock
    private CsvGenerator csvGenerator;

    private String filePath = "some/path";

    private UiWorker uiWorker;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.uiWorker = new UiWorker(querier, notifyingRowCallbackHandler, fileSender, csvWriter, filesystemUtil,
                csvGenerator, filePath);
    }

    @Test
    public void testUiWorker() throws WorkFailedException
    {
        ProgressListener csvProgressListener = mock(ProgressListener.class);
        ProgressListener ftpProgressListener = mock(ProgressListener.class);

        uiWorker.doWork(csvProgressListener, ftpProgressListener);

        verify(notifyingRowCallbackHandler).setListener(csvProgressListener);
        verify(querier).processRows(notifyingRowCallbackHandler);
    }

    @Test
    public void testUiWorkerException() throws IOException
    {
        ProgressListener csvProgressListener = mock(ProgressListener.class);
        ProgressListener ftpProgressListener = mock(ProgressListener.class);
        IOException exToThrow = new IOException();
        doThrow(exToThrow).when(csvWriter).open((File) any());
        try
        {
            uiWorker.doWork(csvProgressListener, ftpProgressListener);
            fail("should throw wrapped exception");
        }
        catch (WorkFailedException e)
        {
            assertSame(exToThrow, e.getCause());
            assertEquals("Could not open " + filePath, e.getDisplayMessage());
        }
    }

}
