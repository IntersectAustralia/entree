/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GeospatialDatumTest.java 234 2010-08-12 05:55:33Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 *
 * @version $Rev: 234 $
 */
public class GeospatialDatumTest
{

    @Test
    public void testParsesNotesCorrectlyToDetermineDatum()
    {
        assertEquals(GeospatialDatum.UNKNOWN, GeospatialDatum.getDatum(null));
        assertEquals(GeospatialDatum.UNKNOWN, GeospatialDatum.getDatum(""));
        assertEquals(GeospatialDatum.UNKNOWN, GeospatialDatum.getDatum("any"));
        assertEquals(GeospatialDatum.UNKNOWN, GeospatialDatum.getDatum("GDA-93"));
        assertEquals(GeospatialDatum.UNKNOWN, GeospatialDatum.getDatum("WGS-94"));
        
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah GDA-94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah gda-94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah Gda-94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah GDA 94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah GDA/94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah GDAx94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("GDAx94"));
        
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah MGA-94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah mga-94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah Mga-94 text"));
        assertEquals(GeospatialDatum.GDA_94, GeospatialDatum.getDatum("blah MGAx94 text"));
        
        assertEquals(GeospatialDatum.WGS_84, GeospatialDatum.getDatum("blah WGS-84 text"));
        assertEquals(GeospatialDatum.WGS_84, GeospatialDatum.getDatum("blah wgs-84 text"));
        assertEquals(GeospatialDatum.WGS_84, GeospatialDatum.getDatum("blah Wgs-84 text"));
        assertEquals(GeospatialDatum.WGS_84, GeospatialDatum.getDatum("blah WGSx84 text"));
        
        assertEquals(GeospatialDatum.AGD_66, GeospatialDatum.getDatum("blah AMG-66 text"));
        assertEquals(GeospatialDatum.AGD_84, GeospatialDatum.getDatum("AMG-84 text"));
        assertEquals(GeospatialDatum.AGD_66, GeospatialDatum.getDatum("blah AGD 66"));
        assertEquals(GeospatialDatum.AGD_84, GeospatialDatum.getDatum("aAGD.84b"));
    }
}
