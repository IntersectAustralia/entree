/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: Molodensky.java 223 2010-08-10 02:11:40Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

/**
 * @version $Rev: 223 $
 * 
 *          standard Molodensky datum transformation method
 * 
 * @see http://home.hiwaay.net/~taylorc/bookshelf/math-science/geodesy/datum/transform/molodensky/
 * 
 * @see http://www.colorado.edu/geography/gcraft/notes/datum/gif/molodens.gif
 * 
 */
public class Molodensky
{

    /**
     * @param from
     *            The geodetic position to be translated.
     * @param ref
     *            The reference ellipsoid
     */
    public GeodeticPosition transform(GeodeticPosition from, ReferenceEllipsoid ref)
    {
        double fromF = ref.getF(); // flattening
        double fromA = ref.getA(); // equatorial radius
        double fromEsq = ref.getEsq(); // first eccentricity squared
        double dx = ref.getDx(); //datum to datum shift parameters
        double dy = ref.getDy();
        double dz = ref.getDz();
        double da = ref.getDa();
        double df = ref.getDf();
        
        
        double slat = Math.sin(from.lat());
        double clat = Math.cos(from.lat());
        double slon = Math.sin(from.lon());
        double clon = Math.cos(from.lon());
        double ssqlat = slat * slat;
        double adb = 1.0 / (1.0 - fromF); // "a divided by b"

        // radius of curvature in prime vertical
        double rn = fromA / Math.sqrt(1.0 - fromEsq * ssqlat);
        // radius of curvature in prime meridian
        double rm = fromA * (1. - fromEsq) / Math.pow((1.0 - fromEsq * ssqlat), 1.5);

        // delta lat, lon, h above the reference ellipsoid
        double dlat = (((((-dx * slat * clon - dy * slat * slon) + dz * clat)
                + (da * ((rn * fromEsq * slat * clat) / fromA)))
                + (df * (rm * adb + rn / adb) * slat * clat))) / (rm + from.h());

        double dlon = (-dx * slon + dy * clon) / ((rn + from.h()) * clat);

        double dh = (dx * clat * clon) + (dy * clat * slon) + (dz * slat) - (da * (fromA / rn))
                + ((df * rn * ssqlat) / adb);

        return new GeodeticPosition(from.lon() + dlon, from.lat() + dlat, from.h() + dh);
    }

}
