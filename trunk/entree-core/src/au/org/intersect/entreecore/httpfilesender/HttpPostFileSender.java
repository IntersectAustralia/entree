/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: HttpPostFileSender.java 213 2010-07-30 05:06:35Z georgina $
 */
package au.org.intersect.entreecore.httpfilesender;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.log4j.Logger;

import au.org.intersect.entreecore.filesender.FileSender;
import au.org.intersect.entreecore.filesender.FileSendingException;
import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * 
 * @version $Rev: 213 $
 */
public class HttpPostFileSender implements FileSender
{
    private static final Logger LOG = Logger.getLogger(HttpPostFileSender.class);
    
    private static final String CSV_FILE_TYPE = "text/csv";
    private static final String MAIL_PARAM = "mail";
    private static final String CSV_PARAM = "csv";
    private static final String PASSWORD_PARAM = "password";
    private static final String USERNAME_PARAM = "username";
    private static final String SUCCESS_MESSAGE = "success:";

    private final MultipartFileUploader multipartFileUploader;
    private final String serverUrl;
    private final String username;
    private final String password;
    private final boolean sendMail;

    public HttpPostFileSender(MultipartFileUploader multipartFileUploader, String serverUrl, String username,
            String password, boolean sendMail)
    {
        super();
        this.multipartFileUploader = multipartFileUploader;
        this.serverUrl = serverUrl;
        this.username = username;
        this.password = password;
        this.sendMail = sendMail;
    }

    public void uploadFile(String filePath, ProgressListener progressListener) throws FileSendingException
    {
        try
        {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(USERNAME_PARAM, username);
            parameters.put(PASSWORD_PARAM, password);
            parameters.put(MAIL_PARAM, Boolean.toString(sendMail));

            FileUploadResult result = multipartFileUploader.sendFile(serverUrl, filePath, CSV_PARAM, CSV_FILE_TYPE,
                    parameters, progressListener);

            LOG.info("Result from upload: status code = " + result.getStatusLine().getStatusCode());
            LOG.info("Result from upload: message = ");
            LOG.info(result.getResponseText());
            validateResponse(result);
        }
        catch (Exception e)
        {
            throw new FileSendingException(e);
        }
    }

    private void validateResponse(FileUploadResult result) throws FileSendingException
    {
        StatusLine httpStatus = result.getStatusLine();
        if (httpStatus.getStatusCode() != HttpStatus.SC_OK)
        {
            throw new FileSendingException("Failed to upload file to server, result code = " + httpStatus
                    + " message = " + httpStatus.getReasonPhrase());
        }

        // TODO: could also check for warnings here or do more validations
        String resultText = result.getResponseText();
        if (!resultText.contains(SUCCESS_MESSAGE))
        {
            throw new FileSendingException("Failed to upload file to server, response message = " + resultText);
        }
    }
}
