/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LowerCaseConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

/**
 * 
 * @version $Rev: 253 $
 */
public class LowerCaseConverter extends SingleValueFieldConverter
{

    protected String mapSingleValue(String accid, Object valueObj)
    {
        if (valueObj == null || valueObj.toString().trim().length() == 0)
        {
            return "";
        }
        String valueToConvert = valueObj.toString().trim();
        return valueToConvert.toLowerCase();
    }
}
