/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: LocationFuzzingRulesTest.java 236 2010-08-13 00:22:20Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 
 * @version $Rev: 236 $
 */
public class LocationFuzzingRulesTest
{

    @Test
    public void testHandlesMissingValue()
    {
        LocationFuzzingRules rules = new LocationFuzzingRules(true, "col", ".*R.*");
        assertFalse(rules.shouldFuzzLocation(new HashMap<String, Object>()));
    }

    @Test
    public void testReturnsTrueIfColumnMatchesRegex()
    {
        LocationFuzzingRules rules = new LocationFuzzingRules(true, "col", ".*R.*");
        assertTrue(rules.shouldFuzzLocation(createRow("R")));
        assertTrue(rules.shouldFuzzLocation(createRow("r")));
        assertTrue(rules.shouldFuzzLocation(createRow("asfRasdf")));
        assertTrue(rules.shouldFuzzLocation(createRow("Rfdrs")));
        assertTrue(rules.shouldFuzzLocation(createRow("  R  ")));
        assertTrue(rules.shouldFuzzLocation(createRow("fdsaR")));
        assertFalse(rules.shouldFuzzLocation(createRow(" ")));
        assertFalse(rules.shouldFuzzLocation(createRow(" asdf")));
        assertFalse(rules.shouldFuzzLocation(createRow(" 2343")));
    }

    @Test
    public void testReturnsFalseIfColumnMatchesRegexButSetToNeverFuzz()
    {
        LocationFuzzingRules rules = new LocationFuzzingRules(false, "col", ".*R.*");
        assertFalse(rules.shouldFuzzLocation(createRow("R")));
        assertFalse(rules.shouldFuzzLocation(createRow("r")));
    }

    private Map<String, Object> createRow(String value)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("col", value);
        return map;
    }
}
