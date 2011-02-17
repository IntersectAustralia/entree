/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GridToLatitudeLongitudeConverter.java 234 2010-08-12 05:55:33Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

/**
 * 
 * @version $Rev: 234 $
 */
public class GridToLatitudeLongitudeConverter
{
    private final RedfearnsFormulaWrapper redfearnsFormulaWrapper;
    private final Molodensky molodensky;

    public GridToLatitudeLongitudeConverter(RedfearnsFormulaWrapper redfearnsFormulaWrapper, Molodensky molodensky)
    {
        super();
        this.redfearnsFormulaWrapper = redfearnsFormulaWrapper;
        this.molodensky = molodensky;
    }

    public GeodeticPosition convert(GeospatialDatum datum, String easting, String northing, String zone)
    {
        // convert using redfearn's formula, params is decided based on datum
        RedfearnsFormulaParameters redfearnsParams = RedfearnsFormulaParameters.getParams(datum);
        GeodeticPosition latlong = this.redfearnsFormulaWrapper.convert(zone, easting, northing, redfearnsParams);

        latlong = doMolodenskyShiftIfNecessary(datum, latlong);
        return latlong;
    }

    public boolean areGridValuesOfSufficientPrecision(String zone, String easting, String northing)
    {
        boolean lengthOk = easting.length() == 6 && northing.length() == 7 && zone.length() > 0;
        if (!lengthOk)
        {
            return false;
        }
        return canParseInt(zone) && canParseInt(northing) && canParseInt(easting);
    }

    private GeodeticPosition doMolodenskyShiftIfNecessary(GeospatialDatum datum, GeodeticPosition latlong)
    {
        // if datum unknown we will assume data was recorded in the wgs84+ datum; so we won’t do a shift on the
        // data. Therefore only shift if AGD

        if (datum == GeospatialDatum.AGD_84)
        {
            return molodensky.transform(latlong, ReferenceEllipsoid.AGD84_GDA94);
        }
        else if (datum == GeospatialDatum.AGD_66)
        {
            return molodensky.transform(latlong, ReferenceEllipsoid.AGD66_GDA94);
        }
        else
        {
            // molodensky not required
            return latlong;
        }
    }

    private boolean canParseInt(String zone)
    {
        try
        {
            Integer.parseInt(zone);
            return true;
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
    }

}
