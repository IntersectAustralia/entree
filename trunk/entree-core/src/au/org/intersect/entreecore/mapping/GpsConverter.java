/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GpsConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

/**
 * 
 * @version $Rev: 253 $
 */
public class GpsConverter extends SingleValueFieldConverter
{

    @Override
    protected String mapSingleValue(String accid, Object value)
    {
        boolean booleanVal;
        if (value instanceof Boolean)
        {
            booleanVal = (Boolean) value;
        }
        else
        {
            booleanVal = Boolean.parseBoolean(value.toString());
        }
        if (booleanVal)
        {
            return "GPS";
        }
        else
        {
            return "Unknown";
        }
    }

}
