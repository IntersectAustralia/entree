/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LocationFuzzingRules.java 235 2010-08-12 23:29:06Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import java.util.Map;

/**
 * 
 * @version $Rev: 235 $
 */
public class LocationFuzzingRules
{
    private final boolean shouldFuzzSecretLocations;
    private final String columnToCheck;
    private final String regexToMatch;

    public LocationFuzzingRules(boolean shouldFuzzSecretLocations, String columnToCheck, String regexToMatch)
    {
        super();
        this.shouldFuzzSecretLocations = shouldFuzzSecretLocations;
        this.columnToCheck = columnToCheck;
        this.regexToMatch = regexToMatch;
    }

    public boolean shouldFuzzLocation(Map<String, Object> originalRow)
    {
        Object rowValue = originalRow.get(columnToCheck);
        String conservationStauts = rowValue == null ? "" : rowValue.toString().toUpperCase();
        return shouldFuzzSecretLocations && conservationStauts.matches(regexToMatch);
    }
}
