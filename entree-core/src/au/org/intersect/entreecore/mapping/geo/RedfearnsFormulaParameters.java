/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: RedfearnsFormulaParameters.java 234 2010-08-12 05:55:33Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

/**
 * 
 * @version $Rev: 234 $
 */
public class RedfearnsFormulaParameters
{
    private static double WGS_84_f = 1 / 298.2572236; /* flattening */
    private static double WGS_84_a = 6378137.0; /* major semi_axis in metres */
    private static double WGS_84_k0 = 0.9996; /* central scale factor */

    private static double ANS_f = 1 / 298.25; /* flattening */
    private static double ANS_a = 6378160.0; /* major semi_axis in metres */
    private static double ANS_k0 = 0.9996; /* central scale factor */

    public static RedfearnsFormulaParameters WGS_84 = new RedfearnsFormulaParameters(WGS_84_f, WGS_84_a, WGS_84_k0);
    public static RedfearnsFormulaParameters ANS = new RedfearnsFormulaParameters(ANS_f, ANS_a, ANS_k0);

    private double flattening;
    private double majorSemiAxis;
    private double centralScaleFactor;

    public RedfearnsFormulaParameters(double f, double a, double k0)
    {
        super();
        this.flattening = f;
        this.majorSemiAxis = a;
        this.centralScaleFactor = k0;
    }

    public double getFlattening()
    {
        return flattening;
    }

    public double getMajorSemiAxis()
    {
        return majorSemiAxis;
    }

    public double getCentralScaleFactor()
    {
        return centralScaleFactor;
    }

    public static RedfearnsFormulaParameters getParams(GeospatialDatum datum)
    {
        if (datum == GeospatialDatum.AGD_66 || datum == GeospatialDatum.AGD_84)
        {
            return ANS;
        }
        else
        {
            return WGS_84;
        }
    }
}
