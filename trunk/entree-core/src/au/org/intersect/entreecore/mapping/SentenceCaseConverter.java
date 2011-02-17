/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: SentenceCaseConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

/**
 * 
 * @version $Rev: 253 $
 */
public class SentenceCaseConverter extends SingleValueFieldConverter
{

    protected String mapSingleValue(String accid, Object valueObj)
    {
        if (valueObj == null || valueObj.toString().trim().length() == 0)
        {
            return "";
        }
        String value = valueObj.toString();
        String valueToConvert = value.trim();
        char firstChar = valueToConvert.charAt(0);
        String remaining = valueToConvert.substring(1);
        return String.valueOf(firstChar).toUpperCase() + remaining.toLowerCase();
    }
}
