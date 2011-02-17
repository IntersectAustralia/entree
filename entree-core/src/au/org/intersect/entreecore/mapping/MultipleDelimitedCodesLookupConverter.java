/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: MultipleDelimitedCodesLookupConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @version $Rev: 253 $
 */
public class MultipleDelimitedCodesLookupConverter extends CodeLookupConverter
{
    private static final String COMMA = ",";
    private static final Logger LOG = Logger.getLogger(MultipleDelimitedCodesLookupConverter.class);

    public MultipleDelimitedCodesLookupConverter(CodesDao codesDao, String description)
    {
        super(codesDao, description);
    }

    public String mapSingleValue(String accid, Object valueFromDb)
    {
        Map<String, String> codes = getCodes();
        // accept space, comma, dot, / , & as delimiters
        String normalisedString = valueFromDb.toString();
        normalisedString = normalisedString.replace(" ", COMMA);
        normalisedString = normalisedString.replace(".", COMMA);
        normalisedString = normalisedString.replace("/", COMMA);
        normalisedString = normalisedString.replace("&", COMMA);

        String[] codeList = normalisedString.split(COMMA);
        String output = "";
        for (String code : codeList)
        {
            if (code != null && code.trim().length() > 0)
            {
                String valueForCode = codes.get(code.trim().toUpperCase());
                if (valueForCode == null)
                {
                    LOG.warn("Unknown " + getDescription() + " code: " + valueFromDb + " for " + accid);
                    // just return the raw value, we don't want to try to guess if the other bits of the string are
                    // valid codes
                    return valueFromDb.toString();
                }
                output += (valueForCode == null ? code : valueForCode) + ";";
            }
        }
        // drop off the last semicolon
        if (output.length() > 0)
        {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }
}
