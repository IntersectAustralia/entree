/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 5 2010-05-07 00:25:24Z georgina $
 */
package au.org.intersect.entreecore.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Prepares data for outputting as CSV. The data is passed in as a map, representing a single record. It looks for
 * key-value pairs where the key matches one of the configured header fields. If a key is missing a blank value will be
 * injected.
 * 
 * @version $Rev: 5 $
 */
public class CsvGenerator
{
    private List<String> columnHeadings;

    public CsvGenerator(String csvHeaders)
    {
        super();
        columnHeadings = new ArrayList<String>();
        String[] headers = csvHeaders.split(",");
        columnHeadings.addAll(Arrays.asList(headers));
    }

    public CommaSeparatedValues getHeaderRow()
    {
        List<String> values = getColumnHeadings();
        return new CommaSeparatedValues(values);
    }

    private List<String> getColumnHeadings()
    {
        return this.columnHeadings;
    }

    public CommaSeparatedValues getDataRow(Map<String, String> row)
    {
        List<String> rowValues = new ArrayList<String>();
        for (String itemKey : getColumnHeadings())
        {
            rowValues.add(stripNulls(row.get(itemKey)));
        }
        CommaSeparatedValues csvRow = new CommaSeparatedValues(rowValues);
        return csvRow;
    }

    private String stripNulls(String itemValue)
    {
        return itemValue == null ? "" : itemValue;
    }
}
