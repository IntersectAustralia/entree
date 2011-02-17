/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: CodesDao.java 211 2010-07-30 01:56:38Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import java.util.Map;

/**
 * 
 * @version $Rev: 211 $
 */
public interface CodesDao
{
    Map<String, String> getCodeLookupTable();
}