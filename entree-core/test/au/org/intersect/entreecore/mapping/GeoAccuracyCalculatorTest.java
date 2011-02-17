/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GeoAccuracyCalculatorTest.java 256 2011-02-09 02:39:48Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import au.org.intersect.entreecore.mapping.geo.GeoAccuracyCalculator;
import au.org.intersect.entreecore.mapping.geo.GeospatialDatum;

/**
 * 
 * @version $Rev: 256 $
 */
public class GeoAccuracyCalculatorTest
{
    private GeoAccuracyCalculator calculator = new GeoAccuracyCalculator();

    @Test
    public void testUnknownDatum()
    {
        // year of record doesn't matter
        // 500 if no gps
        assertEquals(500, calculator.calculate(false, 2000, GeospatialDatum.UNKNOWN));
        assertEquals(500, calculator.calculate(false, 1900, GeospatialDatum.UNKNOWN));
        // 475 if gps used
        assertEquals(475, calculator.calculate(true, 2000, GeospatialDatum.UNKNOWN));
        assertEquals(475, calculator.calculate(true, 1900, GeospatialDatum.UNKNOWN));
    }

    @Test
    public void testGda94Datum()
    {
        // subtract 220 if before 1980, subtract 450 if on or after 1980
        // subtract 25 if gps
        assertEquals(280, calculator.calculate(false, 1979, GeospatialDatum.GDA_94));
        assertEquals(50, calculator.calculate(false, 1980, GeospatialDatum.GDA_94));
        assertEquals(50, calculator.calculate(false, 1981, GeospatialDatum.GDA_94));
        assertEquals(50, calculator.calculate(false, 2010, GeospatialDatum.GDA_94));

        assertEquals(255, calculator.calculate(true, 1979, GeospatialDatum.GDA_94));
        assertEquals(25, calculator.calculate(true, 1980, GeospatialDatum.GDA_94));
        assertEquals(25, calculator.calculate(true, 1981, GeospatialDatum.GDA_94));
        assertEquals(25, calculator.calculate(true, 2010, GeospatialDatum.GDA_94));
    }

    @Test
    public void testWgs84Datum()
    {
        // subtract 220 if before 1980, subtract 450 if on or after 1980
        // subtract 25 if gps
        assertEquals(280, calculator.calculate(false, 1979, GeospatialDatum.WGS_84));
        assertEquals(50, calculator.calculate(false, 1980, GeospatialDatum.WGS_84));
        assertEquals(50, calculator.calculate(false, 1981, GeospatialDatum.WGS_84));
        assertEquals(50, calculator.calculate(false, 2010, GeospatialDatum.WGS_84));

        assertEquals(255, calculator.calculate(true, 1979, GeospatialDatum.WGS_84));
        assertEquals(25, calculator.calculate(true, 1980, GeospatialDatum.WGS_84));
        assertEquals(25, calculator.calculate(true, 1981, GeospatialDatum.WGS_84));
        assertEquals(25, calculator.calculate(true, 2010, GeospatialDatum.WGS_84));
    }

    @Test
    public void testAgd66Datum()
    {
        // subtract 220 if after 1980, subtract 440 if before 1980
        // subtract 25 if gps
        assertEquals(60, calculator.calculate(false, 1979, GeospatialDatum.AGD_66));
        assertEquals(60, calculator.calculate(false, 1980, GeospatialDatum.AGD_66));
        assertEquals(280, calculator.calculate(false, 1981, GeospatialDatum.AGD_66));
        assertEquals(280, calculator.calculate(false, 2010, GeospatialDatum.AGD_66));

        assertEquals(35, calculator.calculate(true, 1979, GeospatialDatum.AGD_66));
        assertEquals(35, calculator.calculate(true, 1980, GeospatialDatum.AGD_66));
        assertEquals(255, calculator.calculate(true, 1981, GeospatialDatum.AGD_66));
        assertEquals(255, calculator.calculate(true, 2010, GeospatialDatum.AGD_66));
    }

    @Test
    public void testAgd84Datum()
    {
        // subtract 220 if before 1980, subtract 440 if on or after 1980
        // subtract 25 if gps
        assertEquals(60, calculator.calculate(false, 1979, GeospatialDatum.AGD_84));
        assertEquals(60, calculator.calculate(false, 1980, GeospatialDatum.AGD_84));
        assertEquals(280, calculator.calculate(false, 1981, GeospatialDatum.AGD_84));
        assertEquals(280, calculator.calculate(false, 2010, GeospatialDatum.AGD_84));

        assertEquals(35, calculator.calculate(true, 1979, GeospatialDatum.AGD_84));
        assertEquals(35, calculator.calculate(true, 1980, GeospatialDatum.AGD_84));
        assertEquals(255, calculator.calculate(true, 1981, GeospatialDatum.AGD_84));
        assertEquals(255, calculator.calculate(true, 2010, GeospatialDatum.AGD_84));
    }
}
