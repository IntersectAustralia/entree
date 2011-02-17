/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FileUploadResult.java 178 2010-07-20 01:40:45Z georgina $
 */
package au.org.intersect.entreecore.httpfilesender;

import org.apache.http.StatusLine;

/**
 * 
 * @version $Rev: 178 $
 */
public class FileUploadResult
{

    private StatusLine statusLine;
    private String responseText;

    public void setResponseText(String responseText)
    {
        this.responseText = responseText;
    }

    public StatusLine getStatusLine()
    {
        return statusLine;
    }

    public void setStatusLine(StatusLine statusLine)
    {
        this.statusLine = statusLine;
    }

    public String getResponseText()
    {
        return responseText;
    }

}