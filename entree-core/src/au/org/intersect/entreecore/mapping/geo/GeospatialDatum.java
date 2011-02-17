/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: GeospatialDatum.java 234 2010-08-12 05:55:33Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

/**
 * 
 * @version $Rev: 234 $
 */
public enum GeospatialDatum
{
    GDA_94, WGS_84, AGD_66, AGD_84, UNKNOWN;

    private static final String GDA_94_REGEX = ".*GDA.94.*";
    private static final String MGA_94_REGEX = ".*MGA.94.*";

    private static final String WGS_84_REGEX = ".*WGS.84.*";

    private static final String AMG_66_REGEX = ".*AMG.66.*";
    private static final String AMG_84_REGEX = ".*AMG.84.*";
    private static final String AGD_66_REGEX = ".*AGD.66.*";
    private static final String AGD_84_REGEX = ".*AGD.84.*";

    public static GeospatialDatum getDatum(String notes)
    {
        if (notes == null)
        {
            return UNKNOWN;
        }

        String notesUpper = notes.toUpperCase();

        if (notesUpper.matches(GDA_94_REGEX) || notesUpper.matches(MGA_94_REGEX))
        {
            return GDA_94;
        }

        if (notesUpper.matches(WGS_84_REGEX))
        {
            return WGS_84;
        }

        if (notesUpper.matches(AMG_66_REGEX) || notesUpper.matches(AGD_66_REGEX))
        {
            return AGD_66;
        }

        if (notesUpper.matches(AGD_84_REGEX) || notesUpper.matches(AMG_84_REGEX))
        {
            return AGD_84;
        }

        return UNKNOWN;
    }
}
