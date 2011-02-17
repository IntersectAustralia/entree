/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: AbstractRowMapperTest.java 231 2010-08-11 23:26:37Z georgina $
 */
package au.org.intersect.entreecore.rowmapper;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * @version $Rev: 231 $
 * 
 */
public class AbstractRowMapperTest
{
    private RowMapper mapper;

    @Mock
    private org.springframework.jdbc.core.RowMapper<Map<String, Object>> backingMapper;
    
    @Mock
    private Map<String, String> mockMappedResults;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.mapper = new AbstractRowMapper(backingMapper)
        {
            @Override
            protected Map<String, String> mapFields(Map<String, Object> row)
            {
                return mockMappedResults;
            }
        };
    }

    @Test
    public void testShouldNotThrowExceptionIfAdditionalDataIsSet() throws SQLException
    {
        ResultSet resultSet = mock(ResultSet.class);
        try
        {
            Map<String, String> actual = mapper.mapRow(resultSet);
            
            // Should return results untouched
            assertSame(mockMappedResults, actual);
            verifyZeroInteractions(actual);
        }
        catch (IllegalStateException e)
        {
            fail("should not throw");
        }
    }

}
