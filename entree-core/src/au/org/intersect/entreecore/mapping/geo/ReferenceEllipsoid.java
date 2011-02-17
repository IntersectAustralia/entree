/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: ReferenceEllipsoid.java 217 2010-08-09 04:29:46Z ryan $
 */
package au.org.intersect.entreecore.mapping.geo;

/**
 * @version $Rev: 217 $
 * 
 */
public class ReferenceEllipsoid
{

    // for constants, see GDA pg 35 http://www.icsm.gov.au/gda/gdatm/gdav2.3.pdf (40/66)

    public static final ReferenceEllipsoid AGD66_GDA94 = new ReferenceEllipsoid(6378160, 1 / 298.25, -127.8, -52.3,
            152.9, -23, -0.00000008119);

    public static final ReferenceEllipsoid AGD84_GDA94 = new ReferenceEllipsoid(6378160, 1 / 298.25, -128.5, -53.0,
            153.4, -23, -0.00000008119);

    private final double a;
    private final double f;
    private final double dx;
    private final double dy;
    private final double dz;
    private final double da;
    private final double df;

    public ReferenceEllipsoid(double a, double f, double dx, double dy, double dz, double da, double df)
    {
        this.a = a;
        this.f = f;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.da = da;
        this.df = df;
    }

    /**
     * @return the a
     */
    public double getA()
    {
        return a;
    }

    /**
     * @return the f
     */
    public double getF()
    {
        return f;
    }

    /**
     * @return the dx
     */
    public double getDx()
    {
        return dx;
    }

    /**
     * @return the dy
     */
    public double getDy()
    {
        return dy;
    }

    /**
     * @return the dz
     */
    public double getDz()
    {
        return dz;
    }

    /**
     * @return the da
     */
    public double getDa()
    {
        return da;
    }

    /**
     * @return the df
     */
    public double getDf()
    {
        return df;
    }

    /**
     * 
     * see Geocentric Datum of Australia Technical Manual v2.3 page 41/66 (36)
     * 
     * http://www.icsm.gov.au/gda/gdatm/gdav2.3.pdf
     * 
     * @return eccentricity squared
     */
    public double getEsq()
    {
        return 2 * f - f * f;
    }
}
