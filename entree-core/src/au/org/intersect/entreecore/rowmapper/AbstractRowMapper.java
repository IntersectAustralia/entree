/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: AbstractRowMapper.java 230 2010-08-11 23:23:53Z georgina $
 */
package au.org.intersect.entreecore.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @version $Rev: 230 $
 *
 */
public abstract class AbstractRowMapper implements RowMapper
{
    private final org.springframework.jdbc.core.RowMapper<Map<String, Object>> backingMapper;
    
    public AbstractRowMapper(org.springframework.jdbc.core.RowMapper<Map<String, Object>> backingMapper)
    {
        this.backingMapper = backingMapper;
    }
    
    public Map<String, String> mapRow(ResultSet resultSet) throws SQLException
    {
        Map<String, Object> map = backingMapper.mapRow(resultSet, 0);
        
        return mapFields(map);
    }

    /**
     * does any data-transformations as necessary for each row.
     *  
     * @param additionalData any data as specified in the configuration
     */
    protected abstract Map<String, String> mapFields(Map<String, Object> row);
}
