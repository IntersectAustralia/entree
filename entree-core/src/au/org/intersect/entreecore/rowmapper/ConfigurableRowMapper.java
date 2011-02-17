/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: ConfigurableRowMapper.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.rowmapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.RowMapper;

import au.org.intersect.entreecore.mapping.FieldConverter;
import au.org.intersect.entreecore.mapping.MultiValueConverter;

/**
 * 
 * @version $Rev: 253 $
 */
public class ConfigurableRowMapper extends AbstractRowMapper
{

    private final Map<String, FieldConverter> converters;
    private final List<MultiValueConverter> multiValueConverters;

    public ConfigurableRowMapper(RowMapper<Map<String, Object>> backingMapper, Map<String, FieldConverter> converters,
            List<MultiValueConverter> multiValueConverters)
    {
        super(backingMapper);
        this.converters = converters;
        this.multiValueConverters = multiValueConverters;
    }

    @Override
    protected final Map<String, String> mapFields(Map<String, Object> row)
    {
        Map<String, String> mappedOutput = new HashMap<String, String>();
        for (Entry<String, Object> columnValue : row.entrySet())
        {
            String key = columnValue.getKey();
            Object value = columnValue.getValue();
            mapKeyValuePair(key, value, mappedOutput);
        }

        for (MultiValueConverter converter : multiValueConverters)
        {
            converter.mapValues(row, mappedOutput);
        }

        return mappedOutput;
    }

    protected void mapKeyValuePair(String key, Object value, Map<String, String> fields)
    {
        if (value == null)
        {
            fields.put(key, "");
        }
        else if (converters.containsKey(key))
        {
            FieldConverter converter = converters.get(key);
            fields.putAll(converter.mapValue(fields.get("accid"), key, value));
        }
        else
        {
            fields.put(key, value.toString());
        }
    }
}
