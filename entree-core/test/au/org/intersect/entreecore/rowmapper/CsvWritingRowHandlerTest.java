/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: CsvWritingRowHandlerTest.java 231 2010-08-11 23:26:37Z georgina $
 */
package au.org.intersect.entreecore.rowmapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.entreecore.csv.CommaSeparatedValues;
import au.org.intersect.entreecore.csv.CsvGenerator;
import au.org.intersect.entreecore.csv.CsvWriter;
import au.org.intersect.entreecore.worker.OperationCancelledException;
import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * @version $Rev: 231 $
 * 
 */
public class CsvWritingRowHandlerTest
{
    private static final String INSTITUTION_ID_CODE = "insid";

    private static final String DEFAULT_INSID = "my-insid";

    @Mock
    private RowMapper columnRowMapper;

    @Mock
    private CsvGenerator csvGenerator;

    @Mock
    private CsvWriter csvWriter;

    private CsvWritingRowHandler handler;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.handler = new CsvWritingRowHandler(csvWriter, csvGenerator, columnRowMapper, DEFAULT_INSID);
    }

    @Test
    public void testRowHandlerCorrectlyProcessesRow() throws SQLException
    {
        ResultSet mockResultSet = mock(ResultSet.class);

        Map<String, String> rowMap = createRowMap();
        when(columnRowMapper.mapRow(mockResultSet)).thenReturn(rowMap);
        CommaSeparatedValues csv = createCsv();
        when(csvGenerator.getDataRow(rowMap)).thenReturn(csv);

        handler.processRow(mockResultSet);
        verify(csvWriter).writeLine(csv);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandlerUpdatesListenerAndNumberOfRows() throws SQLException
    {
        ResultSet mockResultSet = mock(ResultSet.class);
        Map<String, String> rowMap = createRowMap();
        when(columnRowMapper.mapRow(mockResultSet)).thenReturn(rowMap, rowMap, rowMap);

        ProgressListener progressListener = mock(ProgressListener.class);
        handler.setListener(progressListener);

        handler.processRow(mockResultSet);
        handler.processRow(mockResultSet);
        handler.processRow(mockResultSet);

        verify(progressListener).updateProgress(1);
        verify(progressListener).updateProgress(2);
        verify(progressListener).updateProgress(3);
        assertEquals(3, handler.getNumProcessedRows());
    }

    @Test
    public void testSetMaxProgress()
    {
        int maxProgress = 23;

        ProgressListener progressListener = mock(ProgressListener.class);
        handler.setListener(progressListener);

        handler.setMaxProgress(maxProgress);

        verify(progressListener).setMaxProgress(maxProgress);
        verifyNoMoreInteractions(progressListener);
    }

    @Test
    public void testSetMaxProgressWithoutListener()
    {
        // It shouldn't throw a NPE if there is no listener set on the handler
        handler.setMaxProgress(423);
    }

    @Test(expected = OperationCancelledException.class)
    public void testRowHandlersThrowsExceptionIfCancelIsRequested() throws SQLException
    {
        ResultSet mockResultSet = mock(ResultSet.class);
        RowMapper columnRowMapper = mock(RowMapper.class);
        CsvGenerator csvGenerator = mock(CsvGenerator.class);
        CsvWriter csvWriter = mock(CsvWriter.class);

        CsvWritingRowHandler handler = new CsvWritingRowHandler(csvWriter, csvGenerator, columnRowMapper,
                DEFAULT_INSID);
        handler.requestCancel();

        handler.processRow(mockResultSet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRowHandlerAddInstitutionIdIfResultSetDoesNotIncludeIt() throws SQLException
    {
        ResultSet mockResultSet = mock(ResultSet.class);

        Map<String, String> rowMap = createRowMap();
        when(columnRowMapper.mapRow(mockResultSet)).thenReturn(rowMap);
        CommaSeparatedValues csv = createCsv();
        ArgumentCaptor<Map> capture = ArgumentCaptor.forClass(Map.class);
        when(csvGenerator.getDataRow(capture.capture())).thenReturn(csv);

        handler.processRow(mockResultSet);
        verify(csvWriter).writeLine(csv);
        Map<String, String> updatedRowMap = capture.getValue();
        assertEquals(DEFAULT_INSID, updatedRowMap.get(INSTITUTION_ID_CODE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRowHandlerSkipsAddingInstitutionIdIfResultSetAlreadyIncludesIt() throws SQLException
    {
        ResultSet mockResultSet = mock(ResultSet.class);

        Map<String, String> rowMap = createRowMap();
        rowMap.put(INSTITUTION_ID_CODE, "uow");
        when(columnRowMapper.mapRow(mockResultSet)).thenReturn(rowMap);
        CommaSeparatedValues csv = createCsv();
        ArgumentCaptor<Map> capture = ArgumentCaptor.forClass(Map.class);
        when(csvGenerator.getDataRow(capture.capture())).thenReturn(csv);

        handler.processRow(mockResultSet);
        verify(csvWriter).writeLine(csv);
        Map<String, String> updatedRowMap = capture.getValue();
        assertEquals("uow", updatedRowMap.get(INSTITUTION_ID_CODE));
    }

    private CommaSeparatedValues createCsv()
    {
        return new CommaSeparatedValues(Arrays.asList("sometext"));
    }

    private Map<String, String> createRowMap()
    {
        Map<String, String> row = new HashMap<String, String>();
        row.put("somekey", "somevalue");
        return row;
    }

}
