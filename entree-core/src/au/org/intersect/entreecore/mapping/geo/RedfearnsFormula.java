/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: RedfearnsFormula.java 235 2010-08-12 23:29:06Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @version $Rev: 235 $
 */
public class RedfearnsFormula
{
    
    /*
     * Relevant fields are: INPUT gridref1_1 = zone (in FoxProw zone)
     *                                                                        _2 = easting (6figure) (in FoxProw amgeast)
     *                                                                        _3 = easting direction (in FoxProw
     *                                                                        _4 = northing (7 figure) (in FoxProw amgnorth)
     *                                                                        _5 = northing direction (in FoxProw
     *                                              OUTPUT lat = latitude
     *                                                         long = longitude
     */
    public Map convertGridReference(Map input, double f, double a, double k0) {
            Map output = new HashMap();
            /*
             * **************************************************************** GRID
             * TO SPHEROID LATITUDE & LONGITUDE FROM AMG GRID REFERENCE USING
             * REDFEARN'S REVERSE FORMULAE "Accurate to better than 1 mm in the
             * Australian Map Grid"
             * **************************************************************** It
             * has been introduced to allow for use of grid references in the South
             * Australian Gazetteer, which were the basis for the positioning of
             * place names. The latitudes and longitudes provided in this Gazetteer
             * are more approximate, just to the nearest minute. Mr Paul Dearden of
             * DENR is acknowledged for supply of text of a series of C-based
             * programs which served to explain the computation of the "foot plant
             * latitude". W.R. Barker 27 October 1995
             */

            if (!input.get("gridref1_5").equals("")) {
                    int gridref1_1 = Integer.parseInt((String) input.get("gridref1_1"));
                    int gridref1_2 = Integer.parseInt((String) input.get("gridref1_2"));
                    String gridref1_3 = (String) input.get("gridref1_3");
                    int gridref1_4 = Integer.parseInt((String) input.get("gridref1_4"));
                    String gridref1_5 = (String) input.get("gridref1_5");


                    double ecc2 = 2 * f - Math.pow(f, 2); /* the eccentricity squared */

                    /*******************************************************************
                     * Calculation of FOOT POINT LATITUDE (fplat, AMG manual: phi' the
                     * latitude for which m = N'/k0 AMG manual p. 24
                     *
                     * input gridref1_4
                     *
                     * derived constants based on f, a
                     */

                    double n = f / (2 - f);

                    double G = (a * (1 - n) * (1 - Math.pow(n, 2)) * (1 + (9 / 4)
                                    * Math.pow(n, 2) + (255 / 64) * Math.pow(n, 4)))
                                    * Math.PI / 180; /* metres */

                    /* if (gridref1_5 == "S") */
                    /*
                     * For some reason the above conditional causes m and therfore lat
                     * to be zero
                     */

                    double m = (gridref1_4 - 10000000) / k0; /*
                                                                                                             * Meridian distance,
                                                                                                             * -ve southwards
                                                                                                             */

                    double sigma = (m / G) * Math.PI / 180;
                    double s2 = Math.sin(sigma * 2); /*
                                                                                             * the degrees must be in
                                                                                             * radians
                                                                                             */
                    double s4 = Math.sin(sigma * 4);
                    double s6 = Math.sin(sigma * 6);
                    double s8 = Math.sin(sigma * 8);

                    double fplat1 = sigma + (3 * n / 2 - 27 * Math.pow(n, 3) / 32) * s2;
                    double fplat2 = (21 * Math.pow(n, 2) / 16 - 55 * Math.pow(n, 4) / 32)
                                    * s4;
                    double fplat3 = (151 * Math.pow(n, 3) / 96) * s6;
                    double fplat4 = (1097 * Math.pow(n, 4) / 512) * s8;
                    double fplat = fplat1 + fplat2 + fplat3 + fplat4;
                    ;

                    /*
                     * fplat = sigma + (3*n/2 - 27* pow(n,3)/32)*s2 + (21* pow(n,2)/16 -
                     * 55* pow(n,4)/32)*s4 + (151* pow(n,3)/96)*s6 + (1097*
                     * pow(n,4)/512)*s8;
                     */

                    /*
                     * advise(-1,-1,"G ".G." metres, eccentricity^2 ".ecc2." sigma
                     * ".sigma);
                     */
                    /*******************************************************************
                     * Calculation of VARIABLES ASSOCIATED WITH FOOT POINT LATITUDE
                     * rho', nu' and psi' from Kym Perkins (1988) rho_nu routine
                     */

                    double v1 = Math.sqrt(1 - ((ecc2 * Math.pow(Math.sin(fplat), 2))));

                    double rhofpl = a * (1 - ecc2) / (Math.pow(v1, 3));
                    double nufpl = a / v1;

                    double psifpl = nufpl / rhofpl;

                    /* advise(-1,-1," fplat ".fplat." psifpl ".psifpl); */

                    /*******************************************************************
                     * Calculation of Lat/Long from AMG Grid from AMG Manual pp. 5,8, 14
                     */

                    /* LATITUDE calculation (radians) */

                    /* calculated variables */

                    /*
                     * It appears gridref1_3 here is actually west, with the 500000
                     * converting t easting
                     */
                    double east = gridref1_2 - 500000;
                    double tfpl = Math.tan(fplat); /* fplat must be in radians */
                    double tk = tfpl / (k0 * rhofpl);

                    /* split computation into component parts */

                    double elat1 = Math.pow(east, 2) / (2 * k0 * nufpl);
                    double elat2 = Math.pow(east, 4)
                                    / (24 * Math.pow(k0, 3) * Math.pow(nufpl, 3));
                    double elat3 = Math.pow(east, 6)
                                    / (720 * Math.pow(k0, 5) * Math.pow(nufpl, 5));
                    double elat4 = Math.pow(east, 8)
                                    / (40320 * Math.pow(k0, 7) * Math.pow(nufpl, 7));
                    double lat2 = -4 * Math.pow(psifpl, 2) + 9 * psifpl
                                    * (1 - Math.pow(tfpl, 2)) + 12 * Math.pow(tfpl, 2);

                    double lat3a = 8 * Math.pow(psifpl, 4)
                                    * (11 - 24 * Math.pow(tfpl, 2));
                    double lat3b = 12 * Math.pow(psifpl, 3)
                                    * (21 - 71 * Math.pow(tfpl, 2));
                    double lat3c = 15 * Math.pow(psifpl, 2)
                                    * (15 - 98 * Math.pow(tfpl, 2) + 15 * Math.pow(tfpl, 4));
                    double lat3d = 180 * psifpl
                                    * (5 * Math.pow(tfpl, 2) - 3 * Math.pow(tfpl, 4)) + 360
                                    * Math.pow(tfpl, 4);
                    double lat3 = lat3a - lat3b + lat3c + lat3d;

                    /*
                     * lat3 = 8*pow(psifpl,4)*(11 - 24*pow(tfpl,2)) -
                     * 12*pow(psifpl,3)*(21 - 71*pow(tfpl,2)) + 15*pow(psifpl,2)*(15 -
                     * 98*pow(tfpl,2) + 15*pow(tfpl,4)) + 180*psifpl*(5*pow(tfpl,2) -
                     * 3*pow(tfpl,4)) + 360*pow(tfpl,4);
                     */

                    double lat4 = 1385 + 3633 * Math.pow(tfpl, 2) + 4095
                                    * Math.pow(tfpl, 4) + 1575 * Math.pow(tfpl, 6);

                    double term1 = tk * elat1; /* Terms match worked example on p.15 */
                    double term2 = tk * elat2 * lat2;
                    double term3 = tk * elat3 * lat3;
                    double term4 = tk * elat4 * lat4;

                    double latrad = fplat - term1 + term2 - term3 + term4; /*
                                                                                                                                     * latitude
                                                                                                                                     * in
                                                                                                                                     * radians
                                                                                                                                     */

                    /* LONGITUDE calculation (radians) */

                    /* calculated variables relating to zone */
                    double loncm = ((gridref1_1 - 31) * 6 + 3) * Math.PI / 180; /*
                                                                                                                                             * longitude
                                                                                                                                             * of
                                                                                                                                             * central
                                                                                                                                             * meridian
                                                                                                                                             */
                    double sfpl = 1 / Math.cos(fplat); /* sec(fplat) */

                    /* split computation into component parts */

                    double elon1 = east / (k0 * nufpl);
                    double elon2 = Math.pow(east, 3)
                                    / (6 * Math.pow(k0, 3) * Math.pow(nufpl, 3));
                    double elon3 = Math.pow(east, 5)
                                    / (120 * Math.pow(k0, 5) * Math.pow(nufpl, 5));
                    double elon4 = Math.pow(east, 7)
                                    / (5040 * Math.pow(k0, 7) * Math.pow(nufpl, 7));
                    double lon2 = psifpl + 2 * Math.pow(tfpl, 2);
                    double lon3 = -4 * Math.pow(psifpl, 3)
                                    * (1 - 6 * Math.pow(tfpl, 2)) + Math.pow(psifpl, 2)
                                    * (9 - 68 * Math.pow(tfpl, 2)) + 72 * psifpl
                                    * Math.pow(tfpl, 2) + 24 * Math.pow(tfpl, 4);
                    double lon4 = 61 + 662 * Math.pow(tfpl, 2) + 1320
                                    * Math.pow(tfpl, 4) + 720 * Math.pow(tfpl, 6);

                    term1 = sfpl * elon1; /* Terms match worked example on p.15 */
                    term2 = sfpl * elon2 * lon2;
                    term3 = sfpl * elon3 * lon3;
                    term4 = sfpl * elon4 * lon4;


                    /* longitude from central meridian in radians */
                    double lonexcm = term1 - term2 + term3 - term4;

                    /* Longitude (radians) w.r. to Greenwich */
                    double lonrad = lonexcm + loncm;

                    /* advise(-1,-1," latrad ".latrad." lonrad ".lonrad); */

                    /* Convert radians to seconds */

                    double calclat = 3600 * latrad * 180 / Math.PI;
                    double calclong = 3600 * lonrad * 180 / Math.PI;

                    /*
                     * advise(-1,-1," calclat ".calclat." secs, calclong is ".calclong."
                     * secs");
                     */

                    if (gridref1_3.equalsIgnoreCase("W")) {
                            calclong = -calclong;
                            // grlong_4 = "W";
                    } else
                    // grlong_4 = "E";
                    if (gridref1_5 == "S") {
                            /* calclat = -calclat; *//*
                                                                                     * Do above calculations make this
                                                                                     * +ve?
                                                                                     */
                            // grlat_4 = "S";
                    } else
                            // grlat_4 = "N";
                            calclat = -calclat;
                    /*
                     * advise(-1,-1," calclat ".calclat." secs, calclong is ".calclong."
                     * secs");
                     */

                    /* WHAT TO DO WITH HEMISPHERE ???????? */
                    /* Establish whether N/S, E/W */
                    /*
                     * IF grlat < 0 grlat = ABS(grlat) grlat_4 = "S" ELSE grlat_4 = "N"
                     * ENDIF IF grlong < 0 grlong = ABS(grlong) grlong_4 = "W" ELSE
                     * grlong_4 = "E" ENDIF
                     */
                    /* MUST DEAL WITH HEMISPHERE ABOVE ============== */

                    /* ... degree, minutes, seconds */
                    /* NOTE; THE MODULO DOES NOT GIVE DECIMALS: DID IT LONG WAY */
                    /*
                     * Test of rounding grlat = 180000 + 600 + 59.6; grlong = 360000 +
                     * 600 + 59.555;
                     */

                    /* Lat: New way of rounding and exact */
                    /* Rounding used to give the grid reference derived lat/longs */
                    // rndgrlat = round(calclat);
                    // grlat_1 = trunc(rndgrlat/3600);
                    // latrmnsec = rndgrlat%3600;
                    // grlat_2 = printf("%02d", trunc(latrmnsec/60));
                    // grlat_3 = printf("%02d", latrmnsec%60);
                    /* Long: New way of rounding and exact */
                    /* Rounding used to give the grid reference derived lat/longs */
                    // rndgrlong = round(calclong);
                    // grlong_1 = trunc(rndgrlong/3600);
                    // longrmnsec = rndgrlong%3600;
                    // grlong_2 = printf("%02d", trunc(longrmnsec/60));
                    // grlong_3 = printf("%02d", longrmnsec%60);

                   
                    output.put("lat",String.valueOf(calclat/3600));
                    output.put("long",String.valueOf(calclong/3600));
            }
           

            return output;
    }

}
