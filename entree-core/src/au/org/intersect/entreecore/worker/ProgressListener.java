/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: ProgressListener.java 213 2010-07-30 05:06:35Z georgina $
 */
package au.org.intersect.entreecore.worker;

/**
 * @version $Rev: 213 $
 * 
 */
public interface ProgressListener
{
    void setMaxProgress(int maxProgress);

    void updateProgress(int currentProgress);
    
    void setFinished();

}
