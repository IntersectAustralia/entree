/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: AltitudeConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import org.apache.log4j.Logger;


/**
 * Utility class to convert altitude from the database into a number (hispid requires just a number). Rules are as
 * follows: 1. accepts any object where the toString representation can be parsed to an Integer or Double. These values
 * are assumed to already be in metres 2. accepts strings with "m" or "M" anywhere to indicate metres. The "m" or "M" is
 * stripped out 3. accepts "sea level" (any case) and converts to zero 4. anything else is not recognised and a blank
 * string is returned
 * 
 * @version $Rev: 253 $
 */
public class AltitudeConverter extends SingleValueFieldConverter
{

    private static final Logger LOG = Logger.getLogger(AltitudeConverter.class);

    /**
     * @param value
     * @return
     */
    @Override
    protected String mapSingleValue(String accid, Object valueFromDatabase)
    {
        if (valueFromDatabase == null)
        {
            return "";
        }
        // TODO: allow "metres" or "METRES" or variations?
        String value;
        if (valueFromDatabase instanceof String)
        {
            value = (String) valueFromDatabase;
            if (value.equalsIgnoreCase("sea level"))
            {
                value = "0";
            }
            value = value.replace("m", "");
            value = value.replace("M", "");
            value = value.trim();
        }
        else
        {
            // its some other data type, just use toString and then see if the string representation can be parsed into
            // an integer or double
            value = valueFromDatabase.toString();
        }
        try
        {
            int valueInMetres = Integer.parseInt(value);
            return Integer.toString(valueInMetres);
        }
        catch (NumberFormatException e)
        {
            return tryParsingAsFloat(value, accid);
        }
    }

    private String tryParsingAsFloat(String valueFromDatabase, String accid)
    {
        try
        {
            Double.parseDouble(valueFromDatabase);
            return valueFromDatabase;
        }
        catch (NumberFormatException e)
        {
            LOG.warn("Unparseable altitude value: " + valueFromDatabase.toString() + " for " + accid);
            return "";
        }
    }
}
