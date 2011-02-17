/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: MapLookupCodesDao.java 245 2010-09-10 03:53:57Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @version $Rev: 245 $
 */
public class MapLookupCodesDao implements CodesDao
{
    private final Map<String, String> codes;

    public MapLookupCodesDao(String listOfCodes)
    {
        this.codes = new HashMap<String, String>();
        parseCodes(listOfCodes.trim());
    }

    public MapLookupCodesDao(Map<String, String> inputCodes)
    {
        this.codes = new HashMap<String, String>();
        for (Entry<String, String> entry : inputCodes.entrySet())
        {
            codes.put(entry.getKey().toUpperCase(), entry.getValue());
        }
    }

    /**
     * @param codesString
     *            code1:name for code1,code2:name for code2
     */
    private void parseCodes(String codesString)
    {
        if (codesString.length() == 0)
        {
            return;
        }
        String[] codeNamePairs = codesString.split(",");
        for (String codeNamePair : codeNamePairs)
        {
            String[] tokens = codeNamePair.split(":", 2);
            if (tokens.length < 2)
            {
                throw new IllegalArgumentException("Badly formed code string: " + codesString);
            }
            String code = tokens[0];
            String name = tokens[1];
            codes.put(code.toUpperCase(), name);
        }
    }

    @Override
    public Map<String, String> getCodeLookupTable()
    {
        return codes;
    }
}
