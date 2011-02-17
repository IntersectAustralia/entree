/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: AltitudeConverterTest.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 *
 * @version $Rev: 253 $
 */
public class AltitudeConverterTest
{

    private AltitudeConverter altitudeConverter = new AltitudeConverter();
    @Test
    public void testConvertsStringsWithMetresCorrectly()
    {
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150m"));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150 m"));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150 M"));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150M"));
        assertEquals("150", altitudeConverter.mapSingleValue(null, " 150 m"));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150 m "));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150  m"));
        //questionable if these should be allowed
        assertEquals("150", altitudeConverter.mapSingleValue(null, "m 150"));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "15m0"));
    }
    
    @Test
    public void testConvertsPlainStringsCorrectly()
    {
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150"));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "150 "));
        assertEquals("150", altitudeConverter.mapSingleValue(null, " 150   "));
        assertEquals("150", altitudeConverter.mapSingleValue(null, "00150"));
    }
    
    @Test
    public void testConvertsStringsWithMetresAndDecimalsCorrectly()
    {
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "150.2m"));
        assertEquals("150.22", altitudeConverter.mapSingleValue(null, "150.22m"));
        assertEquals("150.333", altitudeConverter.mapSingleValue(null, "150.333m"));
        assertEquals("150.0", altitudeConverter.mapSingleValue(null, "150.0m"));
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "150.2 m"));
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "150.2 M"));
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "150.2M"));
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, " 150.2 m"));
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "150.2 m "));
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "150.2  m"));
        //questionable if these should be allowed
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "m 150.2"));
        assertEquals("150.2", altitudeConverter.mapSingleValue(null, "15m0.2"));
    }
    
    @Test
    public void testRejectsUnknownUnitsOrOtherCharacters()
    {
        assertEquals("", altitudeConverter.mapSingleValue(null, "150s"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "15.2s"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "15.3.33"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "150 metres"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "150 asdf"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "150 ft"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "150ft"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "150 feet"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "asdf"));
        assertEquals("", altitudeConverter.mapSingleValue(null, "asdf"));
    }
    
    @Test
    public void testConvertsSeaLevelToZero()
    {
        assertEquals("0", altitudeConverter.mapSingleValue(null, "sea level"));
        assertEquals("0", altitudeConverter.mapSingleValue(null, "Sea level"));
        assertEquals("0", altitudeConverter.mapSingleValue(null, "Sea Level"));
        assertEquals("0", altitudeConverter.mapSingleValue(null, "SEA LEVEL"));
    }
    
    @Test
    public void testConvertsNumberObjectsCorrectly()
    {
        assertEquals("150", altitudeConverter.mapSingleValue(null, 150));
        assertEquals("150", altitudeConverter.mapSingleValue(null, 150L));
        assertEquals("150.55", altitudeConverter.mapSingleValue(null, 150.55D));
        assertEquals("150.55", altitudeConverter.mapSingleValue(null, 150.55F));
    }
    
    @Test
    public void testHandlesNullsAndBlanks()
    {
        assertEquals("", altitudeConverter.mapSingleValue(null, null));
        assertEquals("", altitudeConverter.mapSingleValue(null, ""));
    }
}
