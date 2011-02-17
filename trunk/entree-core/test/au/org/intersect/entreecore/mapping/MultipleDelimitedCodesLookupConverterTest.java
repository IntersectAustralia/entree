/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: MultipleDelimitedCodesLookupConverterTest.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @version $Rev: 253 $
 */
public class MultipleDelimitedCodesLookupConverterTest
{
    private MultipleDelimitedCodesLookupConverter converter;

    @Before
    public void setUp()
    {
        final Map<String, String> codes = new HashMap<String, String>();
        codes.put("C", "Clay");
        codes.put("LD", "Loam deep");
        codes.put("SSK", "Sand skeletal");
        codes.put("OSH", "Organic shallow");

        CodesDao codesDao = mock(CodesDao.class);
        when(codesDao.getCodeLookupTable()).thenReturn(codes);

        converter = new MultipleDelimitedCodesLookupConverter(codesDao, "Soil");
    }

    @Test
    public void testHandlesSingleValuesRegardlessOfCase()
    {
        assertConversion("Ssk", "Sand skeletal");
        assertConversion("ssk", "Sand skeletal");
        assertConversion("SSK", "Sand skeletal");
    }

    @Test
    public void testHandlesSingleValuesWithDelimiter()
    {
        assertConversion(" Ssk", "Sand skeletal");
        assertConversion("  Ssk ", "Sand skeletal");
        assertConversion("Ssk ", "Sand skeletal");
        assertConversion(",Ssk", "Sand skeletal");
        assertConversion(" .Ssk", "Sand skeletal");
        assertConversion("/Ssk  ", "Sand skeletal");
        assertConversion("& Ssk", "Sand skeletal");
        assertConversion(" Ssk,", "Sand skeletal");
        assertConversion("Ssk .", "Sand skeletal");
        assertConversion("Ssk/ ", "Sand skeletal");
        assertConversion("Ssk  &  ", "Sand skeletal");
    }

    @Test
    public void testHandlesAVarietyOfDelimiters()
    {
        // space, comma, dot, forward slash, ampersand C,LD,OSH
        assertConversion("Ssk Ld ", "Sand skeletal;Loam deep");
        assertConversion("Ssk,Ld", "Sand skeletal;Loam deep");
        assertConversion("Ssk.Ld", "Sand skeletal;Loam deep");
        assertConversion("Ssk/Ld", "Sand skeletal;Loam deep");
        assertConversion("Ssk&Ld", "Sand skeletal;Loam deep");

        assertConversion(" Ssk  Ld ", "Sand skeletal;Loam deep");
        assertConversion("Ssk, Ld", "Sand skeletal;Loam deep");
        assertConversion("  Ssk. Ld  ", "Sand skeletal;Loam deep");
        assertConversion("Ssk/Ld", "Sand skeletal;Loam deep");
        assertConversion("Ssk & Ld", "Sand skeletal;Loam deep");

        assertConversion("Ssk,Ld,Osh", "Sand skeletal;Loam deep;Organic shallow");
        assertConversion("Ssk,Ld Osh", "Sand skeletal;Loam deep;Organic shallow");
    }

    @Test
    public void testReturnsRawStringIfAnyCodeDoesNotMap()
    {
        assertConversion("Abc", "Abc");
        assertConversion("Ssk,Abc", "Ssk,Abc");
        assertConversion("Ssk/other stuff", "Ssk/other stuff");
        assertConversion("just words", "just words");
    }

    private void assertConversion(String code, String output)
    {
        assertEquals(output, converter.mapSingleValue(null, code));
    }
}
