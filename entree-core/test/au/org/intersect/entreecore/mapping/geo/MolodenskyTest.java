/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: MolodenskyTest.java 223 2010-08-10 02:11:40Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @version $Rev: 223 $
 * 
 * 
 *          see GDA pg 41/66 (36) http://www.icsm.gov.au/gda/gdatm/gdav2.3.pdf
 */
public class MolodenskyTest
{
    private static final Logger LOG = Logger.getLogger(MolodenskyTest.class);
    
    private Molodensky molodensky = new Molodensky();

    @Test
    public void testAgd66ToGda94()
    {
        LOG.info("1");
        double fromLatDeg = -37.6543222222222222222222; // -37deg39. 15.56"
        double fromLonDeg = 143.925175; // 143deg55. 30.63"
        double fromHeight = 750; // 750

        double toLatDeg = -37.65282777777777777; // -37deg39. 10.18"
        double toLonDeg = 143.926508333333; // 143deg55. 35.43"
        double toHeight = 749;

        GeodeticPosition from = new GeodeticPosition(Math.toRadians(fromLonDeg), Math.toRadians(fromLatDeg),
                fromHeight);

        GeodeticPosition to = molodensky.transform(from, ReferenceEllipsoid.AGD66_GDA94);

        assertEquals("lat", Math.toRadians(toLatDeg), to.lat(), .00005d);
        LOG.info(to.dmsLat());
        assertEquals("lon", Math.toRadians(toLonDeg), to.lon(), .00005d);
        LOG.info(to.dmsLon());
//        assertEquals("height", toHeight, to.h(), .00005d); // TODO height is incorrect for some reason
    }

    @Test
    public void testAgd84ToGda94()
    {
        LOG.info("");
        LOG.info("2");
        double fromLatDeg = -37.65432222222; // -37deg39. 15.56"
        double fromLonDeg = 143.925175; // 143deg55. 30.63"
        double fromHeight = 750; // 750

        double toLatDeg = -37.652825; // -37deg39. 10.17"
        double toLonDeg = 143.92649444444; // 143deg55. 35.38"
        double toHeight = 748; // 748

        GeodeticPosition from = new GeodeticPosition(Math.toRadians(fromLonDeg), Math.toRadians(fromLatDeg),
                fromHeight);

        GeodeticPosition to = molodensky.transform(from, ReferenceEllipsoid.AGD84_GDA94);

        assertEquals("lat", Math.toRadians(toLatDeg), to.lat(), .00005d);
        LOG.info(to.dmsLat());
        assertEquals("lon", Math.toRadians(toLonDeg), to.lon(), .00005d);
        LOG.info(to.dmsLon());
//        assertEquals("height", toHeight, to.h(), .00005d); // TODO height is incorrect for some reason
    }

    /**
     * @see http://www.colorado.edu/geography/gcraft/notes/datum/gif/molodens.gif
     */
    @Test
    public void testSampleFromFormulaSite()
    {
        LOG.info("");
        LOG.info("3");
        double fromLatDeg = 30;
        double fromLonDeg = -100;
        double fromHeight = 232;

        GeodeticPosition from = new GeodeticPosition(Math.toRadians(fromLonDeg), Math.toRadians(fromLatDeg),
                fromHeight);

        ReferenceEllipsoid e = new ReferenceEllipsoid(6378206.4, 1 / 298.257223563, -8, 160, 176, 6378137 - 6378206.4,
                1 / 298.257223563 - 1 / 294.9786982);

        GeodeticPosition to = molodensky.transform(from, e);

        assertEquals("lat", 30.0002239, Math.toDegrees(to.lat()), 0.00005d);
        assertEquals("lon", -100.0003696, Math.toDegrees(to.lon()), 0.00005d);
        assertEquals("hgt", 194.816, to.h(), 0.005d);
        LOG.info(Math.toDegrees(to.lat()));
        LOG.info(Math.toDegrees(to.lon()));
        LOG.info(to.h());
    }

}
