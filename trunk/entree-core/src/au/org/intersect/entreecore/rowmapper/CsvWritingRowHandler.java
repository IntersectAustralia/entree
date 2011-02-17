/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 5 2010-05-07 00:25:24Z georgina $
 */
package au.org.intersect.entreecore.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import au.org.intersect.entreecore.csv.CommaSeparatedValues;
import au.org.intersect.entreecore.csv.CsvGenerator;
import au.org.intersect.entreecore.csv.CsvWriter;
import au.org.intersect.entreecore.datasource.db.NotifyingRowCallbackHandler;
import au.org.intersect.entreecore.worker.Cancellable;
import au.org.intersect.entreecore.worker.OperationCancelledException;
import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * @version $Rev: 5 $
 * 
 */
public class CsvWritingRowHandler implements NotifyingRowCallbackHandler, Cancellable
{
    private static final String INSTITUTION_ID_HEADER = "insid";

    private static final Logger LOG = Logger.getLogger(CsvWritingRowHandler.class);

    private final CsvWriter csvWriter;
    private final CsvGenerator csvGenerator;
    private final RowMapper rowMapper;
    private final String institutionId;

    private ProgressListener progressListener;
    private int numProcessedRows;
    private boolean cancelRequested;

    public CsvWritingRowHandler(CsvWriter csvWriter, CsvGenerator csvGenerator, RowMapper rowMapper,
            String institutionId)
    {
        this.csvWriter = csvWriter;
        this.csvGenerator = csvGenerator;
        LOG.info("Setting row mapper implementation to " + rowMapper.getClass().getName());
        this.rowMapper = rowMapper;
        this.institutionId = institutionId;
    }

    public void processRow(ResultSet resultSet) throws SQLException
    {
        if (cancelRequested)
        {
            LOG.info("Cancel requested on CsvWritingRowHandler, throwing OperationCancelledException");
            throw new OperationCancelledException();
        }

        Map<String, String> rowMap = rowMapper.mapRow(resultSet);

        addInstitutionIdIfNecessary(rowMap);

        CommaSeparatedValues csvData = csvGenerator.getDataRow(rowMap);
        csvWriter.writeLine(csvData);

        numProcessedRows++;
        updateListener();
    }

    /**
     * if the data from the db doesn't include the insid, add the one configured in the properties file
     */
    private void addInstitutionIdIfNecessary(Map<String, String> rowMap)
    {
        if (!rowMap.containsKey(INSTITUTION_ID_HEADER))
        {
            rowMap.put(INSTITUTION_ID_HEADER, institutionId);
        }
    }

    private void updateListener()
    {
        if (this.progressListener != null)
        {
            this.progressListener.updateProgress(numProcessedRows);
        }
    }

    public void setMaxProgress(int maxProgress)
    {
        LOG.info("setting max progress: " + maxProgress);
        if (this.progressListener != null)
        {
            this.progressListener.setMaxProgress(maxProgress);
        }
    }

    public void setListener(ProgressListener progressListener)
    {
        this.progressListener = progressListener;
    }

    public int getNumProcessedRows()
    {
        return this.numProcessedRows;
    }

    public void requestCancel()
    {
        this.cancelRequested = true;
    }

    @Override
    public void resetCancel()
    {
        this.cancelRequested = false;
        this.numProcessedRows = 0;
    }
}
