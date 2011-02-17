/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LocationConverterTest.java 247 2010-09-12 22:27:13Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * 
 * @version $Rev: 247 $
 */
public class LocationConverterTest
{
    private LocationConverter locationConverter;

    @Mock
    private GridToLatitudeLongitudeConverter gridConverter;

    @Mock
    private GeoAccuracyCalculator geoAccuracyCalculator;

    @Mock
    private LocationFuzzer locationFuzzer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        locationConverter = new LocationConverter(geoAccuracyCalculator, gridConverter, locationFuzzer);
    }

    @Test
    public void testReturnsBlankValuesIfInsufficientPrecisionAndShouldFuzz()
    {
        when(geoAccuracyCalculator.calculate(true, 1980, GeospatialDatum.GDA_94)).thenReturn(500);
        when(gridConverter.areGridValuesOfSufficientPrecision("56", "123", "456")).thenReturn(false);

        Map<String, String> output = locationConverter.doConversion(true, GeospatialDatum.GDA_94, 0, "123", "456",
                "56", true);
        assertEquals("", output.get("sge"));
        assertEquals("", output.get("sgn"));
        assertEquals("", output.get("loc"));
        assertEquals("", output.get("geoacy"));
        assertEquals("", output.get("alt"));
        assertEquals(5, output.size());
    }

    @Test
    public void testReturnsGridOnlyIfInsufficientPrecisionAndShouldNotFuzz()
    {
        when(geoAccuracyCalculator.calculate(false, 1980, GeospatialDatum.AGD_84)).thenReturn(500);
        when(gridConverter.areGridValuesOfSufficientPrecision("56", "123", "456")).thenReturn(false);

        Map<String, String> output = locationConverter.doConversion(false, GeospatialDatum.AGD_84, 1980, "123", "456",
                "56", false);
        assertEquals("S123", output.get("sge"));
        assertEquals("S456", output.get("sgn"));
        assertEquals("", output.get("geoacy"));
        assertEquals(3, output.size());
    }

    @Test
    public void testConvertsToLatLongWhenSufficientPrecisionAndShouldNotFuzz()
    {
        when(geoAccuracyCalculator.calculate(false, 1980, GeospatialDatum.AGD_66)).thenReturn(500);
        when(gridConverter.areGridValuesOfSufficientPrecision("56", "123", "456")).thenReturn(true);
        GeodeticPosition position = createGeodeticPosition();
        when(gridConverter.convert(GeospatialDatum.AGD_66, "123", "456", "56")).thenReturn(position);

        Map<String, String> output = locationConverter.doConversion(false, GeospatialDatum.AGD_66, 1980, "123", "456",
                "56", false);

        assertEquals("S123", output.get("sge"));
        assertEquals("S456", output.get("sgn"));
        assertEquals("500", output.get("geoacy"));

        assertEquals(position.lonDeg(), output.get("londeg"));
        assertEquals(position.lonMin(), output.get("lonmin"));
        assertEquals(position.lonSec(), output.get("lonsec"));
        assertEquals(position.lonDir(), output.get("londir"));
        assertEquals(position.latDeg(), output.get("latdeg"));
        assertEquals(position.latMin(), output.get("latmin"));
        assertEquals(position.latSec(), output.get("latsec"));
        assertEquals(position.latDir(), output.get("latdir"));

        assertEquals(11, output.size());
    }

    @Test
    public void testFuzzesAndConvertsToLatLongWhenSufficientPrecisionAndShouldFuzz()
    {
        when(geoAccuracyCalculator.calculate(false, 1980, GeospatialDatum.AGD_66)).thenReturn(500);
        String originalE = "123";
        String originalN = "456";
        when(gridConverter.areGridValuesOfSufficientPrecision("56", originalE, originalN)).thenReturn(true);
        when(locationFuzzer.fuzz(originalE)).thenReturn("789");
        when(locationFuzzer.fuzz(originalN)).thenReturn("987");
        GeodeticPosition position = createGeodeticPosition();
        when(gridConverter.convert(GeospatialDatum.AGD_66, "789", "987", "56")).thenReturn(position);

        Map<String, String> output = locationConverter.doConversion(false, GeospatialDatum.AGD_66, 1980, originalE,
                originalN, "56", true);

        assertEquals("S789", output.get("sge"));
        assertEquals("S987", output.get("sgn"));
        assertEquals("2700", output.get("geoacy"));
        assertEquals("", output.get("loc"));
        assertEquals("", output.get("alt"));

        assertEquals(position.lonDeg(), output.get("londeg"));
        assertEquals(position.lonMin(), output.get("lonmin"));
        assertEquals(position.lonSec(), output.get("lonsec"));
        assertEquals(position.lonDir(), output.get("londir"));
        assertEquals(position.latDeg(), output.get("latdeg"));
        assertEquals(position.latMin(), output.get("latmin"));
        assertEquals(position.latSec(), output.get("latsec"));
        assertEquals(position.latDir(), output.get("latdir"));

        assertEquals(13, output.size());
    }

    /**
     * @return
     */
    private GeodeticPosition createGeodeticPosition()
    {
        return new GeodeticPosition(1234.44, 4566.66, 0);
    }

}
