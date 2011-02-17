/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: MultiValueConverter.java 216 2010-08-04 02:21:00Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import java.util.Map;

/**
 *
 * @version $Rev: 216 $
 */
public interface MultiValueConverter
{

    void mapValues(Map<String, Object> originalRow, Map<String, String> mappedOutput);

}
