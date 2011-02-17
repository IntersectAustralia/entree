/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: CombineFieldValuesConverter.java 216 2010-08-04 02:21:00Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @version $Rev: 216 $
 */
public class CombineFieldValuesConverter implements MultiValueConverter
{
    private final String finalKey;
    private final List<String> keysToCombine;

    public CombineFieldValuesConverter(String finalKey, List<String> keysToCombine)
    {
        this.finalKey = finalKey;
        this.keysToCombine = keysToCombine;
    }

    @Override
    public void mapValues(Map<String, Object> originalRow, Map<String, String> mappedOutput)
    {
        // combine together several columns into one - gets values from the output map incase other conversions were
        // applied earlier
        List<String> values = new ArrayList<String>();
        for (String keyToCombine : keysToCombine)
        {
            String value = mappedOutput.get(keyToCombine);
            if (value != null && value.toString().trim().length() > 0)
            {
                values.add(value.toString());
            }
            mappedOutput.remove(keyToCombine);
        }
        String combinedValue = delimit(values);
        mappedOutput.put(finalKey, combinedValue);
    }

    private String delimit(List<String> values)
    {
        if (values == null || values.isEmpty())
        {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (String value : values)
        {
            buffer.append(value);
            buffer.append("; ");
        }

        String csvString = buffer.toString();
        return csvString.substring(0, csvString.length() - 2);

    }

}
