/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: UoWLocationConverterIntegrationTest.java 237 2010-08-13 06:19:09Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import au.org.intersect.entreecore.mapping.geo.GeoAccuracyCalculator;
import au.org.intersect.entreecore.mapping.geo.GridToLatitudeLongitudeConverter;
import au.org.intersect.entreecore.mapping.geo.LocationConverter;
import au.org.intersect.entreecore.mapping.geo.LocationFuzzer;
import au.org.intersect.entreecore.mapping.geo.LocationFuzzingRules;
import au.org.intersect.entreecore.mapping.geo.Molodensky;
import au.org.intersect.entreecore.mapping.geo.RedfearnsFormulaWrapper;

/**
 * 
 * @version $Rev: 237 $
 */
public class UoWLocationConverterIntegrationTest
{
    private static final String YES = "Y";

    private static final String FUZZED = "FUZZED";

    private static final String BLANK = "BLANK";

    private List<String[]> testCases = new ArrayList<String[]>();

    private UoWLocationConverter converter;

    @Before
    public void setUp() throws IOException
    {
        File file = new File("/temp/tests.csv");
        List<?> linesFromFile = FileUtils.readLines(file);
        for (Object line : linesFromFile)
        {
            String lineStr = (String) line;
            String[] split = lineStr.split(",");
            testCases.add(split);
        }
        testCases.remove(0);

        LocationFuzzingRules fuzzRules = new LocationFuzzingRules(true, "fuzz", YES);
        LocationFuzzer locationFuzzer = new LocationFuzzer();
        GridToLatitudeLongitudeConverter gridConverter = new GridToLatitudeLongitudeConverter(
                new RedfearnsFormulaWrapper(), new Molodensky());
        GeoAccuracyCalculator geoAccuracyCalc = new GeoAccuracyCalculator();
        LocationConverter locConverter = new LocationConverter(geoAccuracyCalc, gridConverter, locationFuzzer);
        converter = new UoWLocationConverter(fuzzRules, locConverter);
    }

    @Ignore
    // temporary so build ignores
    @Test
    public void runTests()
    {
        for (String[] line : testCases)
        {
            Map<String, Object> input = populateInput(line);
            Map<String, String> output = new HashMap<String, String>();
            output.put("loc", "asdf"); // to see if it gets overwritten
            converter.mapValues(input, output);
            validateOutput(output, line);
        }
    }

    private void validateOutput(Map<String, String> output, String[] line)
    {
        String loc = output.get("loc");
        String sge = output.get("sge");
        String sgn = output.get("sgn");

        validateGeoacy(output, line);
        validateLoc(line, loc);
        validateGrid(line, sge, sgn);
        validateLatLong(line, output);

    }

    private void validateGrid(String[] line, String sge, String sgn)
    {
        String expectedGrid = line[6];
        if (BLANK.equals(expectedGrid))
        {
            assertEquals("line error " + Arrays.toString(line), "", sge);
            assertEquals("line error " + Arrays.toString(line), "", sgn);
        }
        else if (FUZZED.equals(expectedGrid))
        {
            assertNotNull(sge);
            assertNotNull(sgn);
            // todo assert ranges
        }
        else
        {
            // as-is
            assertEquals("S283400", sge);
            assertEquals("S6173000", sgn);
        }
    }

    private void validateLatLong(String[] line, Map<String, String> output)
    {
        String longdeg = output.get("londeg");
        String longmin = output.get("lonmin");
        String longsec = output.get("lonsec");
        String longdir = output.get("londir");
        String latdeg = output.get("latdeg");
        String latmin = output.get("latmin");
        String latsec = output.get("latsec");
        String latdir = output.get("latdir");

        String expectedLatLong = line[7];
        if (expectedLatLong.equals(BLANK))
        {
            assertEquals("line error " + Arrays.toString(line), null, longdeg);
            assertEquals("line error " + Arrays.toString(line), null, longmin);
            assertEquals("line error " + Arrays.toString(line), null, longsec);
            assertEquals("line error " + Arrays.toString(line), null, longdir);
            assertEquals("line error " + Arrays.toString(line), null, latdeg);
            assertEquals("line error " + Arrays.toString(line), null, latmin);
            assertEquals("line error " + Arrays.toString(line), null, latsec);
            assertEquals("line error " + Arrays.toString(line), null, latdir);
        }
        else if ("convert".equals(expectedLatLong))
        {
            // todo
        }
        else
        {
            // fuzz+convert
            // todo
        }
    }

    private void validateLoc(String[] line, String loc)
    {
        String expectedLoc = line[8];
        if (expectedLoc.equals(BLANK))
        {
            expectedLoc = "";
        }
        else
        {
            expectedLoc = "asdf";
        }
        assertEquals("line error " + Arrays.toString(line), expectedLoc, loc);
    }

    private void validateGeoacy(Map<String, String> output, String[] line)
    {
        String geoacy = output.get("geoacy");
        String expectedGeoacy = line[17];
        if (expectedGeoacy.equals(BLANK))
        {
            expectedGeoacy = "";
        }
        assertEquals("line error " + Arrays.toString(line), expectedGeoacy, geoacy);
    }

    private Map<String, Object> populateInput(String[] line)
    {
        Map<String, Object> input = new HashMap<String, Object>();
        String datum = line[0];
        String year = line[1];
        String yearString;
        if (YES.equals(year))
        {
            yearString = "1979";
        }
        else
        {
            yearString = "1981";
        }
        String gps = line[2];
        Boolean gpsBool = false;
        if (gps.equals(YES))
        {
            gpsBool = Boolean.TRUE;
        }
        String gridOk = line[3];
        String secret = line[4];
        input.put("cnot", datum + "junk");
        input.put("cdat", createTimestamp(yearString));
        input.put("altdet", gpsBool);
        input.put("sge", "283400");
        input.put("sgn", "6173000");
        input.put("loc", "asdf");

        if (gridOk.equals(YES))
        {
            input.put("Zone", "56");
        }
        else
        {
            input.put("Zone", "asdf");

        }
        if (secret.equals(YES))
        {
            input.put("fuzz", YES);
        }
        return input;
    }

    private Timestamp createTimestamp(String yearString)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(yearString));
        Timestamp ts = new Timestamp(cal.getTimeInMillis());
        return ts;
    }
}
