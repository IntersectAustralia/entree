/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GeodeticPosition.java 221 2010-08-09 06:24:52Z ryan $
 */
package au.org.intersect.entreecore.mapping.geo;

/**
 * @version $Rev: 221 $
 * 
 */
public class GeodeticPosition
{

    private static final int SECONDS_PER_DEGREE = 3600;
    private static final double MINUTES_PER_DEGREE = 60.0;
    
    private final double lat;
    private final double lon;
    private final double h;

    public GeodeticPosition(double lon, double lat, double h)
    {
        this.lat = lat;
        this.lon = lon;
        this.h = h;
    }

    /** latitude in radians */
    public double lat()
    {
        return lat;
    }

    /** longitude in radians */
    public double lon()
    {
        return lon;
    }

    /** height above the ellipsoid in metres */
    public double h()
    {
        return h;
    }
    
    public String lonDeg()
    {
        return "" + Math.abs(deg(lon));
    }
    
    public String lonMin()
    {
        return "" + Math.abs(min(lon));
    }
    
    public String lonSec()
    {
        return "" + Math.abs(seconds(lon));
    }
    
    public String lonDir()
    {
        return lon > 0 ? "E" : "W";
    }
    
    public String dmsLon()
    {
        return String.format("%dd %dm %fs", deg(lon), Math.abs(min(lon)), Math.abs(seconds(lon)));
    }
    
    public String latDeg()
    {
        return "" + Math.abs(deg(lat));
    }
    
    public String latMin()
    {
        return "" + Math.abs(min(lat));
    }
    
    public String latSec()
    {
        return "" + Math.abs(seconds(lat));
    }
    
    public String latDir()
    {
        return lat > 0 ? "N" : "S";
    }    
    
    public String dmsLat()
    {
        return String.format("%dd %dm %fs", deg(lat), Math.abs(min(lat)), Math.abs(seconds(lat)));
    }

    private static int deg(double deg)
    {
        return (int) deg;
    }

    private static int min(double deg)
    {
        double remainder = deg - deg(deg);
        return (int) (remainder * MINUTES_PER_DEGREE);
    }

    private static double seconds(double deg)
    {
        double remainder = deg - deg(deg) - min(deg) / MINUTES_PER_DEGREE;

        return remainder * SECONDS_PER_DEGREE;
    }

}
