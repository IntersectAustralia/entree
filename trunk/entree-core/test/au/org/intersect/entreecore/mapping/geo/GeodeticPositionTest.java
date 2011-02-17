/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GeodeticPositionTest.java 221 2010-08-09 06:24:52Z ryan $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @version $Rev: 221 $
 *
 */
public class GeodeticPositionTest
{

    @Test
    public void testLat()
    {
        double lon = 23.3;
        double lat = 142.4;
        double h = 423;
        GeodeticPosition p = new GeodeticPosition(lon, lat, h);
        assertEquals(lat, p.lat(), 0d);
    }

    @Test
    public void testLon()
    {
        double lon = 23.3;
        double lat = 142.4;
        double h = 423;
        GeodeticPosition p = new GeodeticPosition(lon, lat, h);
        assertEquals(lon, p.lon(), 0d);
    }

    @Test
    public void testH()
    {
        double lon = 23.3;
        double lat = 142.4;
        double h = 423;
        GeodeticPosition p = new GeodeticPosition(lon, lat, h);
        assertEquals(h, p.h(), 0d);
    }

    @Test
    public void testLonDeg()
    {
        GeodeticPosition p;
        double anyLat = 32;
        double anyH = 423;
        
        double lon;
        
        lon = 43.234432423;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("43", p.lonDeg());
        
        lon = -23.4536;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("sign should be omitted", "23", p.lonDeg());
    }

    @Test
    public void testLonMin()
    {
        GeodeticPosition p;
        double anyLat = 32;
        double anyH = 423;
        
        double lon;
        
        lon = 52.34234;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("20", p.lonMin());
        
        lon = -52.34234;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("sign should be omitted", "20", p.lonMin());
    }

    @Test
    public void testLonSec()
    {
        GeodeticPosition p;
        double anyLat = 32;
        double anyH = 423;
        
        double lon;
        
        lon = 52.34234;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertTrue(p.lonSec().startsWith("32.424"));
        
        lon = -52.34234;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertTrue("sign should be omitted", p.lonSec().startsWith("32.424"));
    }

    @Test
    public void testLonDir()
    {
        GeodeticPosition p;
        double anyLat = 20.34;
        double anyH = 423.12;
        
        double lon;
        
        lon = 23.5476;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("E", p.lonDir());
        
        lon = -65.2412343;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("W", p.lonDir());
    }

    @Test
    public void testDmsLon()
    {
        GeodeticPosition p;
        double anyLat = 20.34;
        double anyH = 423.12;
        
        double lon;
        
        lon = 23.5476;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("23d 32m 51.360000s", p.dmsLon());
        
        lon = -65.2412343;
        p = new GeodeticPosition(lon, anyLat, anyH);
        assertEquals("-65d 14m 28.443480s", p.dmsLon());
    }

    @Test
    public void testLatDeg()
    {
        GeodeticPosition p;
        double anyLon = 32;
        double anyH = 423;
        
        double lat;
        
        lat = 64.234432423;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("64", p.latDeg());
        
        lat = -32.4536;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("sign should be omitted", "32", p.latDeg());
    }

    @Test
    public void testLatMin()
    {
        GeodeticPosition p;
        double anyLon = 32;
        double anyH = 423;
        
        double lat;
        
        lat = 52.34234;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("20", p.latMin());

        lat = -52.34234;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("sign should be omitted", "20", p.latMin());
    }

    @Test
    public void testLatSec()
    {
        GeodeticPosition p;
        double anyLon = 32;
        double anyH = 423;
        
        double lat;
        
        lat = 52.34234;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertTrue(p.latSec().startsWith("32.424"));
        
        lat = -52.34234;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertTrue("sign should be omitted", p.latSec().startsWith("32.424"));
    }

    @Test
    public void testLatDir()
    {
        GeodeticPosition p;
        double anyLon = 21.43;
        double anyH = 423.12;
        
        double lat;
        
        lat = 23.5476;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("N", p.latDir());
        
        lat = -65.2412343;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("S", p.latDir());
    }

    @Test
    public void testDmsLat()
    {
        GeodeticPosition p;
        double anyLon = 20.34;
        double anyH = 423.12;
        
        double lat;
        
        lat = 87.5476;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("87d 32m 51.360000s", p.dmsLat());
        
        lat = -12.2412343;
        p = new GeodeticPosition(anyLon, lat, anyH);
        assertEquals("-12d 14m 28.443480s", p.dmsLat());
    }

}
