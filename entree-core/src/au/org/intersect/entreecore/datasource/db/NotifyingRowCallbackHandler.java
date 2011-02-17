/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: NotifyingRowCallbackHandler.java 245 2010-09-10 03:53:57Z ryan $
 */
package au.org.intersect.entreecore.datasource.db;

import org.springframework.jdbc.core.RowCallbackHandler;

import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * 
 * Extends Spring's RowCallbackHandler to allow for a notifying approach.
 * 
 * setMaxProgress will be called before calling processRow.
 * 
 * @version $Rev: 245 $
 * 
 */
public interface NotifyingRowCallbackHandler extends RowCallbackHandler
{
    void setMaxProgress(int maxProgress);

    void setListener(ProgressListener progressListener);
    
    int getNumProcessedRows();
    
    void requestCancel();
    
    void resetCancel();

}
