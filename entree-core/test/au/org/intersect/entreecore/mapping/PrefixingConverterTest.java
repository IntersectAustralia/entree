/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: PrefixingConverterTest.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @version $Rev: 253 $
 */
public class PrefixingConverterTest
{
    private PrefixingConverter converter = new PrefixingConverter("bob: ");

    @Test
    public void testAddsPrefix()
    {
        assertEquals("bob: smith", converter.mapSingleValue(null, "smith"));
        assertEquals("bob: 1", converter.mapSingleValue(null, 1));
    }

    @Test
    public void testHandlesNullAndBlank()
    {
        assertEquals("bob: ", converter.mapSingleValue(null, ""));
        assertEquals("bob: ", converter.mapSingleValue(null, null));
    }
}
