/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 5 2010-05-07 00:25:24Z georgina $
 */
package au.org.intersect.entreecore.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @version $Rev: 5 $
 * 
 */
public class CsvWriter
{
    private static final Logger LOG = Logger.getLogger(CsvWriter.class);

    private PrintWriter printWriter;
    private FileWriter outFile;
    private boolean open;

    public void writeLine(CommaSeparatedValues csv)
    {
        if (!open)
        {
            throw new IllegalStateException(
                    "Cannot write lines to CSV file before it has been opened. Call open() first");
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug(csv.getAsCSV());
        }
        printWriter.println(csv.getAsCSV());
    }

    public void close()
    {
        IOUtils.closeQuietly(outFile);
        IOUtils.closeQuietly(printWriter);
        open = false;
    }

    public void open(File file) throws IOException
    {
        outFile = new FileWriter(file);
        printWriter = new PrintWriter(outFile);
        open = true;
    }
}
