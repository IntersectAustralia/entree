/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LocationFuzzer.java 236 2010-08-13 00:22:20Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import java.util.Random;

/**
 * Adds a random amount between 
 * 
 * @version $Rev: 236 $
 */
public class LocationFuzzer
{
    // this is the maximum possible error distance on the hypotenuse
    public static final int MAX_FUZZ_DISTANCE = 2200;
    private Random random = new Random();

    public String fuzz(String coordinate)
    {
        int coordAsInt = Integer.parseInt(coordinate);
        return Integer.toString(coordAsInt + getErrorToAdd());
    }

    private int getErrorToAdd()
    {
        // we don't want a 0 multiplier because that has no affect on the value(s)
        int[] possibleValues = new int[] {-4, -3, -2, -1, 1, 2, 3, 4};
        int index = random.nextInt(8);
        int multiplier = possibleValues[index];

        // now introduce our error
        int error = multiplier * getRandomInRange(125, 390);
        return error;
    }

    private int getRandomInRange(int i, int j)
    {
        int rangeOfValues = j - i;
        int value = random.nextInt(rangeOfValues + 1);
        return value + i;
    }

    public void setRandom(Random random)
    {
        this.random = random;
    }
}
