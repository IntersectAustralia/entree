/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LocationConverter.java 249 2010-09-21 02:29:22Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @version $Rev: 249 $
 */
public class LocationConverter
{
    private static final String LOCATION_COLUMN = "loc";
    private static final String ALTITUDE_COLUMN = "alt";
    private static final String GEOACY_COLUMN = "geoacy";
    private static final String NORTHINGS_COLUMN = "sgn";
    private static final String EASTINGS_COLUMN = "sge";

    private final GeoAccuracyCalculator geoAccuracyCalculator;
    private final GridToLatitudeLongitudeConverter gridToLatitudeLongitudeConverter;
    private final LocationFuzzer locationFuzzer;

    public LocationConverter(GeoAccuracyCalculator geoAccuracyCalculator,
            GridToLatitudeLongitudeConverter gridToLatitudeLongitudeConverter, LocationFuzzer locationFuzzer)
    {
        super();
        this.geoAccuracyCalculator = geoAccuracyCalculator;
        this.gridToLatitudeLongitudeConverter = gridToLatitudeLongitudeConverter;
        this.locationFuzzer = locationFuzzer;
    }

    public Map<String, String> doConversion(boolean gps, GeospatialDatum datum, int yearOfRecord, String easting,
            String northing, String zone, boolean fuzzLocation)
    {
        Map<String, String> mappedOutput = new HashMap<String, String>();
        int geoAccuracy = geoAccuracyCalculator.calculate(gps, yearOfRecord, datum);

        if (gridToLatitudeLongitudeConverter.areGridValuesOfSufficientPrecision(zone, easting, northing))
        {
            // we have sufficient info to attempt a conversion
            String newEasting = easting;
            String newNorthing = northing;
            if (fuzzLocation)
            {
                // since we're hiding the real location, fuzz the coordinates before doing any conversions
                newEasting = locationFuzzer.fuzz(easting);
                newNorthing = locationFuzzer.fuzz(northing);
                // update the geoaccuracy to reflect that we've fuzzed the location
                geoAccuracy += LocationFuzzer.MAX_FUZZ_DISTANCE;
                // remove location and altitude since they might describe the precise location
                mappedOutput.put(LOCATION_COLUMN, "");
                mappedOutput.put(ALTITUDE_COLUMN, "");
            }
            GeodeticPosition position = gridToLatitudeLongitudeConverter.convert(datum, newEasting, newNorthing, zone);
            addLatLongToOutput(mappedOutput, position);

            mappedOutput.put(EASTINGS_COLUMN, getWithPrefix(newEasting));
            mappedOutput.put(NORTHINGS_COLUMN, getWithPrefix(newNorthing));
            mappedOutput.put(GEOACY_COLUMN, Integer.toString(geoAccuracy));
        }
        else
        {
            // we don't have enough info to attempt a conversion, so we only include sge and sgn
            // geoaccuracy is not really known, so we don't include it (as recommended by HISPID spec)
            if (fuzzLocation)
            {
                // don't include any info if we're hiding the location
                mappedOutput.put(LOCATION_COLUMN, "");
                mappedOutput.put(ALTITUDE_COLUMN, "");
                mappedOutput.put(EASTINGS_COLUMN, "");
                mappedOutput.put(NORTHINGS_COLUMN, "");
                mappedOutput.put(GEOACY_COLUMN, "");
            }
            else
            {
                // include easting and northing without any modification
                mappedOutput.put(EASTINGS_COLUMN, getWithPrefix(easting));
                mappedOutput.put(NORTHINGS_COLUMN, getWithPrefix(northing));
                mappedOutput.put(GEOACY_COLUMN, "");
            }
        }
        return mappedOutput;
    }

    private String getWithPrefix(String value)
    {
        if (value.length() == 0)
        {
            return "";
        }
        else
        {
            return "S" + value;
        }
    }

    private void addLatLongToOutput(Map<String, String> mappedOutput, GeodeticPosition position)
    {
        mappedOutput.put("londeg", position.lonDeg());
        mappedOutput.put("lonmin", position.lonMin());
        mappedOutput.put("lonsec", position.lonSec());
        mappedOutput.put("londir", position.lonDir());
        mappedOutput.put("latdeg", position.latDeg());
        mappedOutput.put("latmin", position.latMin());
        mappedOutput.put("latsec", position.latSec());
        mappedOutput.put("latdir", position.latDir());
    }

}
