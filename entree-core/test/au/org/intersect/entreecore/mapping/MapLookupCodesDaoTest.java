/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: MapLookupCodesDaoTest.java 241 2010-08-24 02:00:45Z georgina $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 
 * @version $Rev: 241 $
 */
public class MapLookupCodesDaoTest
{

    @Test
    public void testCorrectlyParsesCodes()
    {
        MapLookupCodesDao dao = new MapLookupCodesDao("ABC:def,GHI:jk:l");
        Map<String, String> lookup = dao.getCodeLookupTable();
        assertEquals(2, lookup.size());
        assertEquals("def", lookup.get("ABC"));
        assertEquals("jk:l", lookup.get("GHI"));
    }

    @Test
    public void testHandlesSingleValueOk()
    {
        MapLookupCodesDao dao = new MapLookupCodesDao("ABC:def");
        Map<String, String> lookup = dao.getCodeLookupTable();
        assertEquals(1, lookup.size());
        assertEquals("def", lookup.get("ABC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsIllegalArgumentExceptionForBadlyFormedString()
    {
        new MapLookupCodesDao("ABC:def,ABD");
    }

    @Test
    public void testUppercasesTheKey()
    {
        MapLookupCodesDao dao = new MapLookupCodesDao("aBC:def,Ghi:jkl");
        Map<String, String> lookup = dao.getCodeLookupTable();
        assertEquals(2, lookup.size());
        assertEquals("def", lookup.get("ABC"));
        assertEquals("jkl", lookup.get("GHI"));
    }

    @Test
    public void testUppercasesKeysWhenConstructingWithMap()
    {
        Map<String, String> myMap = new HashMap<String, String>();
        myMap.put("abc", "def");
        myMap.put("gHi", "jkl");
        MapLookupCodesDao dao = new MapLookupCodesDao(myMap);
        Map<String, String> lookup = dao.getCodeLookupTable();
        assertEquals(2, lookup.size());
        assertEquals("def", lookup.get("ABC"));
        assertEquals("jkl", lookup.get("GHI"));
    }
}
