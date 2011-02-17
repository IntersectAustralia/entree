/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: EntreeWorker.java 88 2010-06-28 03:16:43Z ryan $
 */
package au.org.intersect.entreecore.worker;

/**
 * @version $Rev: 88 $
 * 
 */
public interface EntreeWorker
{

    /**
     * @throws WorkFailedException if anything went wrong.
     * @return number of processed rows
     */
    int doWork(ProgressListener csvProgressListener, ProgressListener ftpProgressListener) throws WorkFailedException;
    
    void requestCancel();
    
}
