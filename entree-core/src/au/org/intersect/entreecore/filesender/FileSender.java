/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FileSender.java 190 2010-07-22 00:57:02Z ryan $
 */
package au.org.intersect.entreecore.filesender;

import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * @version $Rev: 190 $
 * 
 */
public interface FileSender
{
    /**
     * @throws FileSendingException 
     */
    void uploadFile(String filePath, ProgressListener progressListener) throws FileSendingException;

}
