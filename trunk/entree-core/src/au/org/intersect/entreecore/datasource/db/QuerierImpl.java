/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: QuerierImpl.java 202 2010-07-28 22:54:45Z georgina $
 */
package au.org.intersect.entreecore.datasource.db;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @version $Rev: 202 $
 * 
 */
public class QuerierImpl implements Querier
{
    private final JdbcTemplate jdbcTemplate;
    private final String sql;
    private final String countingSql;

    public QuerierImpl(JdbcTemplate jdbcTemplate, String sql, String countingSql)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.sql = sql;
        this.countingSql = countingSql;
    }
    
    /**
     * @param notifyingRowCallbackHandler the callback which calls methods on one row
     */
    public void processRows(NotifyingRowCallbackHandler notifyingRowCallbackHandler)
    {
        int numRows = jdbcTemplate.queryForInt(countingSql);
        notifyingRowCallbackHandler.setMaxProgress(numRows);
        
        jdbcTemplate.query(sql, notifyingRowCallbackHandler);
    }
}
