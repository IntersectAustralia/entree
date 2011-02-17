/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 5 2010-05-07 00:25:24Z georgina $
 */
package au.org.intersect.entreecore.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @version $Rev: 5 $
 * 
 */
public class CsvGeneratorTest
{
    @Test
    public void testShouldCreateCorrectHeaderRow() throws IOException
    {
        CsvGenerator generator = new CsvGenerator("accid,insid");
        assertNotNull(generator.getHeaderRow());
        assertRowValues(generator.getHeaderRow(), "accid", "insid");
    }

    @Test
    public void testCorrectlyMapsFromInputToDataRows()
    {
        // text
        Map<String, String> record1 = createInputRecord("abc", "def");
        // empty map
        Map<String, String> record4 = new HashMap<String, String>();

        CsvGenerator generator = new CsvGenerator("accid,insid");

        assertRowValues(generator.getDataRow(record1), "abc", "def");
        assertRowValues(generator.getDataRow(record4), "", "");
    }

    private Map<String, String> createInputRecord(String accid, String insid)
    {
        Map<String, String> row = new HashMap<String, String>();
        row.put("accid", accid);
        row.put("insid", insid);
        row.put("junk", "morejunk");
        return row;
    }

    private void assertRowValues(CommaSeparatedValues row, String... values)
    {
        assertArrayEquals(row.getValues().toArray(), values);
    }
}
