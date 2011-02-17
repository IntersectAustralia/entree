/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: RowMapper.java 230 2010-08-11 23:23:53Z georgina $
 */
package au.org.intersect.entreecore.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * to be implemented by university-specific RowMappers
 * 
 * @version $Rev: 230 $
 * 
 */
public interface RowMapper
{
    /**
     * maps a single row
     * 
     * @throws SQLException
     */
    Map<String, String> mapRow(ResultSet resultSet) throws SQLException;

}
