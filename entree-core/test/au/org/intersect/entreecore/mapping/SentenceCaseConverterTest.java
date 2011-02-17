/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: SentenceCaseConverterTest.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 *
 * @version $Rev: 253 $
 */
public class SentenceCaseConverterTest
{
    private SentenceCaseConverter converter = new SentenceCaseConverter();

    @Test
    public void testConvertsFirstCharacterToUpperCaseAndOtherToLower()
    {
        assertEquals("Bluegum", converter.mapSingleValue(null, "bluegum"));
        assertEquals("Bluegum", converter.mapSingleValue(null, "Bluegum"));
        assertEquals("Bluegum", converter.mapSingleValue(null, "BLUEGUM"));
        assertEquals("Bluegum", converter.mapSingleValue(null, "bLuegum"));
        assertEquals("Bluegum", converter.mapSingleValue(null, "BlueGum"));
    }
    
    @Test
    public void testTrimsLeadingAndTrailingSpaces()
    {
        assertEquals("Bluegum", converter.mapSingleValue(null, " bluegum"));
        assertEquals("Bluegum", converter.mapSingleValue(null, "  Bluegum"));
        assertEquals("Bluegum", converter.mapSingleValue(null, "BLUEGUM "));
        assertEquals("Bluegum", converter.mapSingleValue(null, " bLuegum "));
    }
    
    @Test
    public void testHandlesShortWords()
    {
        assertEquals("B", converter.mapSingleValue(null, "b"));
    }
    
    @Test
    public void testHandlesNonAlphaCharacters()
    {
        assertEquals("123", converter.mapSingleValue(null, "123"));
        assertEquals("1ab", converter.mapSingleValue(null, "1AB"));
        assertEquals("$%#.,", converter.mapSingleValue(null, "$%#.,"));
    }
    
    @Test
    public void testHandlesNullAndEmpty()
    {
        assertEquals("", converter.mapSingleValue(null, null));
        assertEquals("", converter.mapSingleValue(null, ""));
        assertEquals("", converter.mapSingleValue(null, "  "));
    }
}
