/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: InfraspecificRankConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * @version $Rev: 253 $
 */
public class InfraspecificRankConverter implements FieldConverter
{
    private static final String SUBFORM = "subf";
    private static final String SUBVARIETY = "subvar";
    private static final String VARIETY = "var";
    private static final String FORM = "f";
    private static final String SUBSPECIES = "subsp";

    private static final String FORM_ALTERNATE_NAME = "form";
    private static final String SUBSPECIES_ALTERNATE_NAME = "ssp";

    private static final Logger LOG = Logger.getLogger(InfraspecificRankConverter.class);

    private final Set<String> legalTypes = new HashSet<String>();

    public InfraspecificRankConverter()
    {
        legalTypes.add(SUBSPECIES);
        legalTypes.add(VARIETY);
        legalTypes.add(SUBVARIETY);
        legalTypes.add(FORM);
        legalTypes.add(SUBFORM);
    }

    /**
     * Convert infraspecific rank info from a single field as a string to its separate components
     */
    @Override
    public Map<String, String> mapValue(String accid, String key, Object valueObj)
    {
        // if its empty just return blanks
        if (valueObj == null || valueObj.toString().trim().length() == 0)
        {
            return getMap("", "");
        }
        String value = valueObj.toString();
        // if it doesn't contain a space, we can't parse it so return blanks and warn
        if (!value.trim().contains(" "))
        {
            LOG.warn("Unparseable sub-species value: " + value);
            return getMap("", "");
        }
        String valueToConvert = value.trim();
        int firstSpace = valueToConvert.indexOf(' ');
        String type = valueToConvert.substring(0, firstSpace).trim().toLowerCase();
        String name = valueToConvert.substring(firstSpace).trim().toLowerCase();

        // drop a full stop if its there
        if (type.endsWith("."))
        {
            type = type.substring(0, type.length() - 1);
        }
        // special cases: convert ssp to subsp and form to f
        if (type.equals(SUBSPECIES_ALTERNATE_NAME))
        {
            type = SUBSPECIES;
        }
        if (type.equals(FORM_ALTERNATE_NAME))
        {
            type = FORM;
        }
        // check its not empty and is one of the 5 allowed values
        if (type.length() == 0 || !legalTypes.contains(type))
        {
            LOG.warn("Unparseable sub-species value [" + value + "]" + " for " + accid);
            return getMap("", "");
        }
        else
        {
            return getMap(type + ".", name);
        }
    }

    private Map<String, String> getMap(String isprk, String isp)
    {
        Map<String, String> result = new HashMap<String, String>();
        result.put("isprk", isprk);
        result.put("isp", isp);
        return result;
    }

}
