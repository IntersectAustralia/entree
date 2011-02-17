/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: SingleValueFieldConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import java.util.Collections;
import java.util.Map;

/**
 * 
 * @version $Rev: 253 $
 */
public abstract class SingleValueFieldConverter implements FieldConverter
{

    @Override
    public Map<String, String> mapValue(String accid, String key, Object value)
    {
        String mappedValue = value == null ? "" : mapSingleValue(accid, value);
        return Collections.singletonMap(key, mappedValue);
    }

    /**
     * @param accid TODO
     * @param value
     * @return
     */
    protected abstract String mapSingleValue(String accid, Object value);

}
