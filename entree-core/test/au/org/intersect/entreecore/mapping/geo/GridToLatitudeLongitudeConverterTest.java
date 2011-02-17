/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GridToLatitudeLongitudeConverterTest.java 234 2010-08-12 05:55:33Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * 
 * @version $Rev: 234 $
 */
public class GridToLatitudeLongitudeConverterTest
{
    @Mock
    private RedfearnsFormulaWrapper redfearn;

    @Mock
    private Molodensky molodensky;

    private GridToLatitudeLongitudeConverter converter;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        converter = new GridToLatitudeLongitudeConverter(redfearn, molodensky);
    }

    @Test
    public void testUsesWgsParamsNoMolodenskyIfDatumIsWgs()
    {
        GeodeticPosition position = new GeodeticPosition(100, 200, 300);
        when(redfearn.convert("56", "123456", "1234567", RedfearnsFormulaParameters.WGS_84)).thenReturn(position);
        GeodeticPosition output = converter.convert(GeospatialDatum.WGS_84, "123456", "1234567", "56");
        assertSame(position, output);
    }

    @Test
    public void testUsesWgsParamsNoMolodenskyIfDatumIsGda()
    {
        GeodeticPosition originalPosition = new GeodeticPosition(100, 200, 300);
        when(redfearn.convert("56", "123456", "1234567", RedfearnsFormulaParameters.WGS_84)).thenReturn(
                originalPosition);
        GeodeticPosition output = converter.convert(GeospatialDatum.GDA_94, "123456", "1234567", "56");
        assertSame(originalPosition, output);
    }

    @Test
    public void testUsesAnsParamsAndMolodenskyIfDatumIsAgd66()
    {
        GeodeticPosition originalPosition = new GeodeticPosition(100, 200, 300);
        GeodeticPosition shiftedPosition = new GeodeticPosition(1100, 1200, 1300);
        when(redfearn.convert("56", "123456", "1234567", RedfearnsFormulaParameters.ANS)).thenReturn(
                originalPosition);
        when(molodensky.transform(originalPosition, ReferenceEllipsoid.AGD66_GDA94)).thenReturn(shiftedPosition);
        GeodeticPosition output = converter.convert(GeospatialDatum.AGD_66, "123456", "1234567", "56");
        assertSame(shiftedPosition, output);
    }

    @Test
    public void testUsesAnsParamsAndMolodenskyIfDatumIsAgd84()
    {
        GeodeticPosition originalPosition = new GeodeticPosition(100, 200, 300);
        GeodeticPosition shiftedPosition = new GeodeticPosition(1100, 1200, 1300);
        when(redfearn.convert("56", "123456", "1234567", RedfearnsFormulaParameters.ANS)).thenReturn(
                originalPosition);
        when(molodensky.transform(originalPosition, ReferenceEllipsoid.AGD84_GDA94)).thenReturn(shiftedPosition);
        GeodeticPosition output = converter.convert(GeospatialDatum.AGD_84, "123456", "1234567", "56");
        assertSame(shiftedPosition, output);
    }

    @Test
    public void testUsesWgsParamsAndNoMolodenskyIfDatumIsUnknown()
    {
        GeodeticPosition originalPosition = new GeodeticPosition(100, 200, 300);
        when(redfearn.convert("56", "123456", "1234567", RedfearnsFormulaParameters.WGS_84)).thenReturn(
                originalPosition);
        GeodeticPosition output = converter.convert(GeospatialDatum.UNKNOWN, "123456", "1234567", "56");
        assertSame(originalPosition, output);
    }

    @Test
    public void testOnlyAllowsWellFormedGridReferences()
    {
        // must be 6 digits for easting, 7 for northing
        assertFalse(converter.areGridValuesOfSufficientPrecision("56", "12345", "1234567"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("56", "123456", "123456"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("56", "1234567", "1234567"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("56", "123456", "12345678"));
        // all must parse to int
        assertFalse(converter.areGridValuesOfSufficientPrecision("5f", "123456", "1234567"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("56", "123f456", "1234567"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("56", "123456", "123f4567"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("", "123456", "1234567"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("5a", "", "1234567"));
        assertFalse(converter.areGridValuesOfSufficientPrecision("56", "123456", ""));
        // valid
        assertTrue(converter.areGridValuesOfSufficientPrecision("56", "123456", "1234567"));
    }
}
