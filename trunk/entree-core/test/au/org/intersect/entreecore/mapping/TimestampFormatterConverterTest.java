/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: TimestampFormatterConverterTest.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.junit.Test;

/**
 * 
 * @version $Rev: 253 $
 */
public class TimestampFormatterConverterTest
{
    private TimestampFormatterConverter converter = new TimestampFormatterConverter();

    @Test
    public void testReturnsRawValueIfFieldNotATimestamp()
    {
        assertEquals("asdf", converter.mapSingleValue(null, "asdf"));
    }

    @Test
    public void testReturnsCorrectlyFormattedTimestamp()
    {
        Timestamp ts = new Timestamp(1280457078084L);
        assertEquals("20100730", converter.mapSingleValue(null, ts));
    }
}
