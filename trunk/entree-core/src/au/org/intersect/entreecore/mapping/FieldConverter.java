/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FieldConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import java.util.Map;

/**
 * 
 * @version $Rev: 253 $
 */
public interface FieldConverter
{
    /**
     * Map the raw value to a HISPID compliant value using appropriate transformation rules. The resulting HISPID
     * key/value pair(s) should be returned as a Map. This caters for the case where a single raw value is split into
     * more than one HISPID field.
     * @param accid TODO
     * @param key
     *            the field key from the database
     * @param value
     *            the raw value from the database
     */
    Map<String, String> mapValue(String accid, String key, Object value);
}
