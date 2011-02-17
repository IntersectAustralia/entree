/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LowerCaseConverterTest.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 *
 * @version $Rev: 253 $
 */
public class LowerCaseConverterTest
{
    private LowerCaseConverter converter = new LowerCaseConverter();

    @Test
    public void testConvertsAllToLower()
    {
        assertEquals("bluegum", converter.mapSingleValue(null, "bluegum"));
        assertEquals("bluegum", converter.mapSingleValue(null, "bluegum"));
        assertEquals("bluegum", converter.mapSingleValue(null, "BLUEGUM"));
        assertEquals("bluegum", converter.mapSingleValue(null, "bLuegum"));
        assertEquals("bluegum", converter.mapSingleValue(null, "BlueGum"));
    }
    
    @Test
    public void testTrimsLeadingAndTrailingSpaces()
    {
        assertEquals("bluegum", converter.mapSingleValue(null, " bluegum"));
        assertEquals("bluegum", converter.mapSingleValue(null, "  bluegum"));
        assertEquals("bluegum", converter.mapSingleValue(null, "BLUEGUM "));
        assertEquals("bluegum", converter.mapSingleValue(null, " bLuegum "));
    }
    
    @Test
    public void testHandlesShortWords()
    {
        assertEquals("b", converter.mapSingleValue(null, "b"));
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
