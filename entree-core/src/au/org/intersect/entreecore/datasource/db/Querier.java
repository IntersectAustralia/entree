/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: Querier.java 40 2010-06-21 02:18:50Z ryan $
 */
package au.org.intersect.entreecore.datasource.db;


/**
 * @version $Rev: 40 $
 *
 */
public interface Querier
{

    /**
     * @return list of tuples associated with this querier
     */
    void processRows(NotifyingRowCallbackHandler notifyingRowCallbackHandler);
    
}
