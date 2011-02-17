/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: TrackableFileBody.java 213 2010-07-30 05:06:35Z georgina $
 */
package au.org.intersect.entreecore.httpfilesender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.content.FileBody;

import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * 
 * @version $Rev: 213 $
 */
public class TrackableFileBody extends FileBody
{

    private static final double ONE_POINT_ONE = 1.1D;
    private static final int BUFFER_SIZE = 4096;
    private ProgressListener progressListener;

    public TrackableFileBody(File file, String mimeType, ProgressListener progressListener)
    {
        super(file, mimeType);
        this.progressListener = progressListener;
    }

    @Override
    public void writeTo(final OutputStream out) throws IOException
    {
        if (out == null)
        {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        progressListener.setMaxProgress(getMaxProgress());
        InputStream in = new FileInputStream(getFile());
        int iterations = 0;
        try
        {
            byte[] tmp = new byte[BUFFER_SIZE];
            int l;
            while ((l = in.read(tmp)) != -1)
            {
                out.write(tmp, 0, l);
                iterations++;
                progressListener.updateProgress(iterations);
            }
            out.flush();
        }
        finally
        {
            in.close();
        }
    }

    private int getMaxProgress()
    {
        long bytesToSend = super.getContentLength();
        double iterations = (double) bytesToSend / (double) BUFFER_SIZE;
        // add 10 percent since there's a delay after we finish sending the file
        double iterationsPlus10Percent = iterations * ONE_POINT_ONE;
        return (int) iterationsPlus10Percent;
    }
}
