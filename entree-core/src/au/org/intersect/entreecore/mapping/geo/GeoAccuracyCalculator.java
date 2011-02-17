/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GeoAccuracyCalculator.java 256 2011-02-09 02:39:48Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

/**
 * 
 * @version $Rev: 256 $
 */
public class GeoAccuracyCalculator
{
    public int calculate(boolean gps, int yearOfRecord, GeospatialDatum datum)
    {
        // start at 500 for unknown datum and possible conversion
        int geoAccuracy = 500;

        // if gps was used, subtract 500
        if (gps)
        {
            geoAccuracy -= 25;
        }

        if (datum == GeospatialDatum.GDA_94 || datum == GeospatialDatum.WGS_84)
        {
            // a conversion has occurred, and we don't know how it was done
            // we'll allow for either (a) an unnecessary transform in the accuracy; or (b) an incorrect datum
            if (yearOfRecord < 1980)
            {
                geoAccuracy -= 220;
            }
            // it is likely that everything is as it seems – keep 50m for 1/20th of a grid-square or gps error
            else
            {
                geoAccuracy -= 450;
            }
        }
        // we couldn't specifically match to the wgs84/gda94 datums, so check for other datums. If found, they will
        // still result in a conversion, but the error will be reduced.
        else if (datum == GeospatialDatum.AGD_66 || datum == GeospatialDatum.AGD_84)
        {
            // there is a chance that the datum reported is not correct
            if (yearOfRecord > 1980)
            {
                geoAccuracy -= 220;
            }
            // it is likely that everything is as it seems
            // we still need to allow for the error in molodensky’s conversion
            else
            {
                geoAccuracy -= 440;
            }
        }
        return geoAccuracy;
    }

}
