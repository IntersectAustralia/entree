/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: QuerierImplTest.java 198 2010-07-26 05:32:52Z georgina $
 */
package au.org.intersect.entreecore.datasource.db;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @version $Rev: 198 $
 *
 */
public class QuerierImplTest
{
    @Mock
    private JdbcTemplate jdbcTemplate;
    
    private QuerierImpl querierImpl;
    
    private final String sql = "some sql string";
    
    private final String countingSql = "select count(*) from somewhere";
    
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.querierImpl = new QuerierImpl(jdbcTemplate, sql, countingSql);
    }
    
    @Test
    public void testProcessRows() throws SQLException
    {
        int maxProgress = 23;
        when(jdbcTemplate.queryForInt(anyString())).thenReturn(maxProgress);
        NotifyingRowCallbackHandler rowCallbackHandler = mock(NotifyingRowCallbackHandler.class);
        querierImpl.processRows(rowCallbackHandler);
        verify(jdbcTemplate).queryForInt(countingSql);
        
        verify(rowCallbackHandler).setMaxProgress(maxProgress);
        verify(jdbcTemplate).query(sql, rowCallbackHandler);
        verifyNoMoreInteractions(rowCallbackHandler);
    }
}

