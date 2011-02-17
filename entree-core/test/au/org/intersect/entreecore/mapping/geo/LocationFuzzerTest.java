/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LocationFuzzerTest.java 236 2010-08-13 00:22:20Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.Test;

/**
 * 
 * @version $Rev: 236 $
 */
public class LocationFuzzerTest
{

    @Test
    public void testReturnsCorrectValue()
    {
        LocationFuzzer fuzzer = new LocationFuzzer();
        Random random = mock(Random.class);
        fuzzer.setRandom(random);

        when(random.nextInt(8)).thenReturn(2); // -2 as multiplier
        when(random.nextInt(266)).thenReturn(150); // 275 as error
        // result is (-2*275) + 12345 =

        String result = fuzzer.fuzz("12345");
        assertEquals("11795", result);
    }

    @Test
    public void testMe()
    {
        LocationFuzzer fuzzer = new LocationFuzzer();
        for (int i = 0; i < 1000; i++)
        {
            assertInRange(fuzzer.fuzz("0"));
        }
    }

    /**
     * @param fuzz
     */
    private void assertInRange(String fuzz)
    {
        Integer fuzzFactor = Integer.parseInt(fuzz);
        if (fuzzFactor < 0)
        {
            assertTrue("bad fuzz factor " + fuzzFactor, fuzzFactor <= -125);
            assertTrue("bad fuzz factor " + fuzzFactor, fuzzFactor >= -1560);
        }
        else
        {
            assertTrue("bad fuzz factor " + fuzzFactor, fuzzFactor >= 125);
            assertTrue("bad fuzz factor " + fuzzFactor, fuzzFactor <= 1560);
        }
    }

}
