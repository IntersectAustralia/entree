/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: UoWLocationConverter.java 241 2010-08-24 02:00:45Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import au.org.intersect.entreecore.mapping.geo.GeospatialDatum;
import au.org.intersect.entreecore.mapping.geo.LocationConverter;
import au.org.intersect.entreecore.mapping.geo.LocationFuzzingRules;

/**
 * 
 * @version $Rev: 241 $
 */
public class UoWLocationConverter implements MultiValueConverter
{
    private static final String RECORD_DATE_COLUMN = "cdat";
    private static final String ZONE_COLUMN = "Zone";
    private static final String NORTHINGS_COLUMN = "sgn";
    private static final String EASTINGS_COLUMN = "sge";
    private static final String GPS_COLUMN = "altdet";
    private static final String NOTES_COLUMN = "cnot";

    private final LocationFuzzingRules locationFuzzingRules;
    private final LocationConverter locationConverter;

    public UoWLocationConverter(LocationFuzzingRules locationFuzzingRules, LocationConverter locationConverter)
    {
        super();
        this.locationFuzzingRules = locationFuzzingRules;
        this.locationConverter = locationConverter;
    }

    @Override
    public void mapValues(Map<String, Object> originalRow, Map<String, String> mappedOutput)
    {
        String notes = getColumnValueAsString(originalRow, NOTES_COLUMN).toUpperCase();
        boolean gps = getWasGpsUsed(originalRow, notes);
        GeospatialDatum datum = GeospatialDatum.getDatum(notes);
        int yearOfRecord = getYearOfRecord(originalRow);
        String easting = getColumnValueAsString(originalRow, EASTINGS_COLUMN);
        String northing = getColumnValueAsString(originalRow, NORTHINGS_COLUMN);
        String zone = getColumnValueAsString(originalRow, ZONE_COLUMN);
        boolean fuzzLocation = locationFuzzingRules.shouldFuzzLocation(originalRow);
        mappedOutput.put("fuzz",  Boolean.toString(fuzzLocation));

        Map<String, String> locationInfo = locationConverter.doConversion(gps, datum, yearOfRecord, easting, northing,
                zone, fuzzLocation);
        mappedOutput.putAll(locationInfo);
    }

    private int getYearOfRecord(Map<String, Object> originalRow)
    {
        Timestamp collectionDate = (Timestamp) originalRow.get(RECORD_DATE_COLUMN);
        if (collectionDate == null)
        {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(collectionDate.getTime());
        return cal.get(Calendar.YEAR);
    }

    private String getColumnValueAsString(Map<String, Object> originalRow, String columnName)
    {
        return originalRow.get(columnName) == null ? "" : originalRow.get(columnName).toString().trim();
    }

    private boolean getWasGpsUsed(Map<String, Object> originalRow, String notes)
    {
        boolean valueOfGpsColumn = getColumnValueAsBoolean(originalRow, GPS_COLUMN);
        if (valueOfGpsColumn)
        {
            return true;
        }
        return notes.contains("GPS");
    }

    private boolean getColumnValueAsBoolean(Map<String, Object> originalRow, String columnName)
    {
        Object value = originalRow.get(columnName);
        if (value instanceof Boolean)
        {
            return (Boolean) value;
        }
        else
        {
            String valueString = value.toString();
            return Boolean.parseBoolean(valueString);
        }
    }
}
