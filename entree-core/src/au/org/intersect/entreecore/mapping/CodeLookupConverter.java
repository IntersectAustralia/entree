/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: CodeLookupConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @version $Rev: 253 $
 */
public class CodeLookupConverter extends SingleValueFieldConverter
{
    private static final Logger LOG = Logger.getLogger(CodeLookupConverter.class);

    private final CodesDao codesDao;
    private final String description;
    private Map<String, String> codes;

    public CodeLookupConverter(CodesDao codesDao, String description)
    {
        super();
        this.codesDao = codesDao;
        this.description = description;
    }

    protected Map<String, String> getCodes()
    {
        if (codes == null)
        {
            codes = codesDao.getCodeLookupTable();
        }
        return codes;
    }

    @Override
    protected String mapSingleValue(String accid, Object valueObj)
    {
        getCodes();
        String value = valueObj.toString();

        if (codes.containsKey(value.toUpperCase()))
        {
            return codes.get(value.toUpperCase());
        }
        else
        {
            LOG.warn("Unknown " + description + " code: " + value + " for " + accid);
            return value;
        }
    }

    public String getDescription()
    {
        return description;
    }
}
