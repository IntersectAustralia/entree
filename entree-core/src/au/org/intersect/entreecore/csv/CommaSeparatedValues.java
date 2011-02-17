/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 5 2010-05-07 00:25:24Z georgina $
 */
package au.org.intersect.entreecore.csv;

import java.util.List;

/**
 * 
 * @version $Rev: 5 $
 */
public class CommaSeparatedValues
{

    private static final String DOUBLE_QUOTE = "\"";
    private final List<String> values;

    public CommaSeparatedValues(List<String> values)
    {
        this.values = values;
    }

    public String getAsCSV()
    {
        if (values == null || values.isEmpty())
        {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (String value : values)
        {
            buffer.append(DOUBLE_QUOTE);
            buffer.append(escapeDoubleQuotes(value));
            buffer.append(DOUBLE_QUOTE);
            buffer.append(",");
        }

        String csvString = buffer.toString();
        return csvString.substring(0, csvString.length() - 1);
    }

    private String escapeDoubleQuotes(String value)
    {
        return value.replace(DOUBLE_QUOTE, "\"\"");
    }

    public List<String> getValues()
    {
        return values;
    }
}
