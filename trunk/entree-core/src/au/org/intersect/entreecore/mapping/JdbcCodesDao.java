/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: JdbcCodesDao.java 211 2010-07-30 01:56:38Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @version $Rev: 211 $
 */
public class JdbcCodesDao implements CodesDao
{
    private final JdbcTemplate jdbcTemplate;
    private final String keyColumn;
    private final String valueColumn;
    private final String databaseQuery;

    public JdbcCodesDao(JdbcTemplate jdbcTemplate, String databaseQuery, String keyColumnName, String valueColumnName)
    {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.databaseQuery = databaseQuery;
        this.keyColumn = keyColumnName;
        this.valueColumn = valueColumnName;
    }

    public Map<String, String> getCodeLookupTable()
    {
        Map<String, String> codes = new HashMap<String, String>();
        List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(databaseQuery);
        for (Map<String, Object> map : queryResult)
        {
            String key = (String) map.get(keyColumn);
            String value = (String) map.get(valueColumn);
            codes.put(key.toUpperCase(), value); //make keys upper case so we can do case insensitive find
        }
        return codes;
    }

}
