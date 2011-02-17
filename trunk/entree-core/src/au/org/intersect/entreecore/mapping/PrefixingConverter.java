/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: PrefixingConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

/**
 * 
 * @version $Rev: 253 $
 */
public class PrefixingConverter extends SingleValueFieldConverter
{
    private String prefix;

    public PrefixingConverter(String prefix)
    {
        super();
        this.prefix = prefix;
    }

    @Override
    protected String mapSingleValue(String accid, Object valueObj)
    {
        String value = valueObj == null ? "" : valueObj.toString();
        return prefix + value;
    }
}
