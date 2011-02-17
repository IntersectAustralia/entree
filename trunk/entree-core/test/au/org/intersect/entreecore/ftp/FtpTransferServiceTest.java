/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FtpTransferServiceTest.java 190 2010-07-22 00:57:02Z ryan $
 */
package au.org.intersect.entreecore.ftp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.entreecore.filesender.FileSendingException;
import au.org.intersect.entreecore.worker.ProgressListener;
import au.org.intersect.filetransfer.TransferException;
import au.org.intersect.filetransfer.TransferService;
import au.org.intersect.filetransfer.TransferStatus;

/**
 * @version $Rev: 190 $
 *
 */
public class FtpTransferServiceTest
{
    @Mock
    private TransferService transferService;
    
    private final String fileToUpload = "some/file.csv";
    
    private final String destination = "/home/fdsa/fdsa.csv";
    
    private FtpTransferService ftpTransferService;
    
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        
        this.ftpTransferService = new FtpTransferService(transferService, destination);
    }

    @Test
    public void testUploadFile() throws FileSendingException
    {
        TransferStatus partiallyDoneStatus = mock(TransferStatus.class);
        when(partiallyDoneStatus.isCompleted()).thenReturn(false);
        
        when(partiallyDoneStatus.getTotalTransferred()).thenReturn(3L);
        when(partiallyDoneStatus.getTotalToTransfer()).thenReturn(5L);
                
        TransferStatus completeStatus = mock(TransferStatus.class);
        when(completeStatus.isCompleted()).thenReturn(true);
        when(completeStatus.getTotalTransferred()).thenReturn(5L);
        when(completeStatus.getTotalToTransfer()).thenReturn(5L);
        
        when(transferService.getTransferStatus()).thenReturn(null, null, partiallyDoneStatus,
                completeStatus);
        
        ProgressListener progressListener = mock(ProgressListener.class);
        
        ftpTransferService.uploadFile(fileToUpload, progressListener);
        
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        verify(transferService).transfer(fileCaptor.capture(), eq(destination));
        assertEquals(new File(fileToUpload).getName(), fileCaptor.getValue().getName());
        
        ArgumentCaptor<Integer> maxProgressCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(progressListener).setMaxProgress(maxProgressCaptor.capture());
        ArgumentCaptor<Integer> progressCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(progressListener, atLeastOnce()).updateProgress(progressCaptor.capture());
        
        List<Integer> progress = progressCaptor.getAllValues();
        assertEquals(maxProgressCaptor.getValue(), progress.get(progress.size() - 1));

    }
    
    @Test
    public void testUploadFileCompletesOnStatusFailure() throws FileSendingException
    {
        TransferStatus failedStatus = mock(TransferStatus.class);
        when(transferService.getTransferStatus()).thenReturn(failedStatus);
        when(failedStatus.isCompleted()).thenReturn(false);
        when(failedStatus.isFailed()).thenReturn(true);
        
        TransferException exception = mock(TransferException.class);
        when(failedStatus.faliure()).thenReturn(exception);
        
        ProgressListener progressListener = mock(ProgressListener.class);
        
        try
        {
            ftpTransferService.uploadFile(fileToUpload, progressListener);
            fail("It should throw a wrapped Exception");
        }
        catch (FileSendingException e)
        {
            assertSame(exception, e.getCause());
        }
    }

}
