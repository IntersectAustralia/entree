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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @version $Rev: 5 $
 * 
 */
public class CsvWriterTest
{
    /**
     * 
     */
    private static final String CSV_FILENAME = "test.csv";
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testCsvWriterWritesCorrectLinesToFile() throws IOException
    {
        CsvWriter writer = new CsvWriter();

        CommaSeparatedValues line1 = new CommaSeparatedValues(Arrays.asList("a", "b", "c"));
        CommaSeparatedValues line2 = new CommaSeparatedValues(Arrays.asList("abc", "def", "ghi"));
        CommaSeparatedValues line3 = new CommaSeparatedValues(Arrays.asList("a\"bc", "d,ef", "g'hi"));
        File file = folder.newFile(CSV_FILENAME);

        writer.open(file);
        writer.writeLine(line1);
        writer.writeLine(line2);
        writer.writeLine(line3);
        writer.close();

        List<?> linesFromFile = FileUtils.readLines(file);
        assertEquals(3, linesFromFile.size());
        assertEquals("\"a\",\"b\",\"c\"", linesFromFile.get(0));
        assertEquals("\"abc\",\"def\",\"ghi\"", linesFromFile.get(1));
        assertEquals("\"a\"\"bc\",\"d,ef\",\"g'hi\"", linesFromFile.get(2));
    }

    @Test
    public void testCsvWriterWritesEmptyFileIfNoLines() throws IOException
    {
        CsvWriter writer = new CsvWriter();
        File file = folder.newFile(CSV_FILENAME);
        writer.open(file);
        writer.close();

        List<?> linesFromFile = FileUtils.readLines(file);
        assertEquals(0, linesFromFile.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testCantWriteLinesIfNotOpened()
    {
        CsvWriter writer = new CsvWriter();
        writer.writeLine(new CommaSeparatedValues(Arrays.asList("test")));
    }

    @Test(expected = IllegalStateException.class)
    public void testCantWriteLinesIfAlreadyClosed() throws IOException
    {
        CsvWriter writer = new CsvWriter();
        writer.open(folder.newFile(CSV_FILENAME));
        writer.close();
        writer.writeLine(new CommaSeparatedValues(Arrays.asList("test")));
    }

    @Test
    public void testCloseCanBeCalledAtAnyTime() throws IOException
    {
        CsvWriter writer = new CsvWriter();
        writer.close();
        writer.open(folder.newFile(CSV_FILENAME));
        writer.close();
        writer.close();

    }
}
