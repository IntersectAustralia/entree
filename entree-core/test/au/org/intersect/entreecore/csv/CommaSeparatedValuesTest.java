/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 5 2010-05-07 00:25:24Z georgina $
 */
package au.org.intersect.entreecore.csv;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @version $Rev: 5 $
 * 
 */
public class CommaSeparatedValuesTest
{

    @Test
    public void testGetAsCSVSurroundsItemsWithDoubleQuotesAndDoesntPutCommaAtEnd()
    {
        CommaSeparatedValues csv = new CommaSeparatedValues(createValues("a", "b", "c"));
        assertEquals("\"a\",\"b\",\"c\"", csv.getAsCSV());
    }

    @Test
    public void testGetAsCSVEscapesDoubleQuotesByPutting2DoubleQuotes()
    {
        CommaSeparatedValues csv = new CommaSeparatedValues(createValues("a\"bc", "b", "c"));
        assertEquals("\"a\"\"bc\",\"b\",\"c\"", csv.getAsCSV());
    }

    @Test
    public void testGetAsCSVReturnsJustTheStringIfOnlyOneValue()
    {
        CommaSeparatedValues csv = new CommaSeparatedValues(createValues("abc"));
        assertEquals("\"abc\"", csv.getAsCSV());
    }

    @Test
    public void testGetAsCSVReturnsBlankStringIfValuesNull()
    {
        CommaSeparatedValues csv = new CommaSeparatedValues(null);
        assertEquals("", csv.getAsCSV());
    }

    @Test
    public void testGetAsCSVReturnsBlankStringIfValuesEmpty()
    {
        CommaSeparatedValues csv = new CommaSeparatedValues(new ArrayList<String>());
        assertEquals("", csv.getAsCSV());
    }

    private List<String> createValues(String... strings)
    {
        return Arrays.asList(strings);
    }
}
