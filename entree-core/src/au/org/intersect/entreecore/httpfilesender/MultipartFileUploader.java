/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: MultipartFileUploader.java 213 2010-07-30 05:06:35Z georgina $
 */
package au.org.intersect.entreecore.httpfilesender;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * 
 * @version $Rev: 213 $
 */
public class MultipartFileUploader
{
    private static final Logger LOG = Logger.getLogger(MultipartFileUploader.class);

    public FileUploadResult sendFile(String url, String filePath, String fileParameterName, String fileType,
            Map<String, String> parameters, ProgressListener progressListener) throws ClientProtocolException,
        IOException
    {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httpPost = new HttpPost(url);

        MultipartEntity multipartEntity = createMultipartEntity(filePath, fileParameterName, fileType, parameters,
                progressListener);

        httpPost.setEntity(multipartEntity);

        LOG.info("Executing request " + httpPost.getRequestLine());

        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        FileUploadResult result = handleResponse(response, responseEntity);

        httpclient.getConnectionManager().shutdown();
        progressListener.setFinished();
        return result;
    }

    private FileUploadResult handleResponse(HttpResponse response, HttpEntity responseEntity) throws IOException
    {
        FileUploadResult result = new FileUploadResult();
        result.setStatusLine(response.getStatusLine());
        if (responseEntity != null)
        {
            result.setResponseText(EntityUtils.toString(responseEntity));
        }
        if (responseEntity != null)
        {
            responseEntity.consumeContent();
        }
        return result;
    }

    private MultipartEntity createMultipartEntity(String filePath, String fileParameterName, String fileType,
            Map<String, String> parameters, ProgressListener progressListener) throws UnsupportedEncodingException
    {
        MultipartEntity multipartEntity = new MultipartEntity();
        for (Entry<String, String> param : parameters.entrySet())
        {

            multipartEntity.addPart(param.getKey(), new StringBody(param.getValue()));
        }
        File file = new File(filePath);
        ContentBody fileContent = new TrackableFileBody(file, fileType, progressListener);
        multipartEntity.addPart(fileParameterName, fileContent);
        return multipartEntity;
    }
}
