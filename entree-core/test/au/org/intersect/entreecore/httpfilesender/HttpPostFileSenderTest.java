/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: HttpPostFileSenderTest.java 213 2010-07-30 05:06:35Z georgina $
 */
package au.org.intersect.entreecore.httpfilesender;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpVersion;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.entreecore.filesender.FileSendingException;
import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * 
 * @version $Rev: 213 $
 */
@SuppressWarnings("unchecked")
public class HttpPostFileSenderTest
{

    private static final String FILE_PATH = "file/path";
    private static final int SC_200 = 200;
    private static final int SC_404 = 404;

    private HttpPostFileSender sender;

    @Mock
    private MultipartFileUploader uploader;

    @Mock
    private ProgressListener progressListener;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        sender = new HttpPostFileSender(uploader, "server-url", "username", "password", true);
    }

    @Test(expected = FileSendingException.class)
    public void testThrowsExceptionIfStatusCodeIsNot200() throws FileSendingException, IOException
    {
        FileUploadResult result = createResult(SC_404, "success:it worked ok");
        when(
                uploader.sendFile(anyString(), anyString(), anyString(), anyString(), any(Map.class),
                        any(ProgressListener.class))).thenReturn(result);
        sender.uploadFile(FILE_PATH, progressListener);
    }

    @Test(expected = FileSendingException.class)
    public void testThrowsExceptionIfResponseMessageDoesNotIncludeSuccess() throws IOException, FileSendingException
    {
        FileUploadResult result = createResult(SC_200, "blah:blah");
        when(
                uploader.sendFile(anyString(), anyString(), anyString(), anyString(), any(Map.class),
                        any(ProgressListener.class))).thenReturn(result);
        sender.uploadFile(FILE_PATH, progressListener);
    }

    @Test(expected = FileSendingException.class)
    public void testPassesCorrectParametersToUploader() throws IOException, FileSendingException
    {
        FileUploadResult result = createResult(SC_200, "blah:blah");
        ArgumentCaptor<Map> mapArg = ArgumentCaptor.forClass(Map.class);
        when(
                uploader.sendFile(eq("server-url"), eq(FILE_PATH), eq("csv"), eq("text/csv"), mapArg.capture(),
                        any(ProgressListener.class))).thenReturn(result);
        sender.uploadFile(FILE_PATH, progressListener);

        Map<String, String> params = mapArg.getValue();
        assertEquals("fred", params.get("username"));
        assertEquals("bloggs", params.get("password"));
        assertEquals("true", params.get("mail"));
    }

    @Test(expected = FileSendingException.class)
    public void testWrapsExceptions() throws IOException, FileSendingException
    {
        when(
                uploader.sendFile(anyString(), anyString(), anyString(), anyString(), any(Map.class),
                        any(ProgressListener.class))).thenThrow(new IOException());
        sender.uploadFile(FILE_PATH, progressListener);

    }

    private FileUploadResult createResult(int statusCode, String message)
    {
        FileUploadResult result = new FileUploadResult();
        result.setResponseText(message);
        result.setStatusLine(new BasicStatusLine(HttpVersion.HTTP_1_1, statusCode, "ok"));
        return result;
    }
}
