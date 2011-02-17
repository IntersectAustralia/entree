/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: CombineFieldValuesConverterTest.java 216 2010-08-04 02:21:00Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @version $Rev: 216 $
 */
public class CombineFieldValuesConverterTest
{

    private CombineFieldValuesConverter combiner;

    @Before
    public void setUp() throws Exception
    {
        combiner = new CombineFieldValuesConverter("key2", Arrays.asList("key1", "key2", "key3", "key4"));
    }

    @Test
    public void testCombinesFieldsWhenAllPresent()
    {
        Map<String, Object> row = new HashMap<String, Object>();
        Map<String, String> output = new HashMap<String, String>();
        output.put("key2", "fred");
        output.put("key3", "bob");
        output.put("key1", "sue");
        output.put("key4", "joe");
        combiner.mapValues(row, output);
        assertEquals("sue; fred; bob; joe", output.get("key2"));
        assertNull(output.get("key1"));
        assertNull(output.get("key3"));
        assertNull(output.get("key4"));
    }

    @Test
    public void testCombinesToEmptyStringWhenAllMissing()
    {
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, String> output = new HashMap<String, String>();
        combiner.mapValues(data, output);
        assertEquals("", output.get("key2"));
        assertNull(output.get("key1"));
        assertNull(output.get("key3"));
        assertNull(output.get("key4"));
    }

    @Test
    public void testHandlesNullsAndMissingValuesAndEmptyStrings()
    {
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, String> output = new HashMap<String, String>();
        output.put("key1", null);
        output.put("key2", "");
        output.put("key4", "stuff");
        combiner.mapValues(data, output);
        assertEquals("stuff", output.get("key2"));
        assertNull(output.get("key1"));
        assertNull(output.get("key3"));
        assertNull(output.get("key4"));
    }

}
