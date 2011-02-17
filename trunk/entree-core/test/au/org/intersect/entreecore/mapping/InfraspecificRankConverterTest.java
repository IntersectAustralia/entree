/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: InfraspecificRankConverterTest.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

/**
 * 
 * @version $Rev: 253 $
 */
public class InfraspecificRankConverterTest
{

    private InfraspecificRankConverter converter = new InfraspecificRankConverter();

    @Test
    public void testReturnsBlanksForEmptyStringOrNull()
    {
        assertMap("", "", converter.mapValue(null, "isp", ""));
        assertMap("", "", converter.mapValue(null, "isp", null));
    }

    @Test
    public void testReturnsBlanksForStringWithoutSpace()
    {
        assertMap("", "", converter.mapValue(null, "isp", "blah"));
        assertMap("", "", converter.mapValue(null, "isp", " blah"));
        assertMap("", "", converter.mapValue(null, "isp", " blah "));
        assertMap("", "", converter.mapValue(null, "isp", "blah "));
    }

    @Test
    public void testCorrectlySplitsStringWithOrWithoutFullStop()
    {
        assertMap("subsp.", "spinosa", converter.mapValue(null, "isp", "subsp spinosa"));
        assertMap("subsp.", "sericeum", converter.mapValue(null, "isp", "subsp.  sericeum"));
        assertMap("subsp.", "aborescens", converter.mapValue(null, "isp", "subsp. aborescens"));
        assertMap("", "", converter.mapValue(null, "isp", "subsp..  sericeum")); // doesn't allow multiple dots
        assertMap("", "", converter.mapValue(null, "isp", ". aborescens")); // just a . is invalid
    }

    @Test
    public void testAllowsValidTypes()
    {
        assertMap("subsp.", "spinosa", converter.mapValue(null, "isp", "subsp spinosa"));
        assertMap("var.", "spinosa", converter.mapValue(null, "isp", "var spinosa"));
        assertMap("subvar.", "spinosa", converter.mapValue(null, "isp", "subvar spinosa"));
        assertMap("f.", "spinosa", converter.mapValue(null, "isp", "f. spinosa"));
        assertMap("subf.", "spinosa", converter.mapValue(null, "isp", "subf. spinosa"));
        // mixed case
        assertMap("subsp.", "spinosa", converter.mapValue(null, "isp", "Subsp spiNosa"));
        assertMap("var.", "spinosa", converter.mapValue(null, "isp", "VAR SPINOSA"));
        assertMap("subvar.", "spinosa", converter.mapValue(null, "isp", "subVar Spinosa"));
        assertMap("f.", "spinosa", converter.mapValue(null, "isp", "F. spinosA"));
        assertMap("subf.", "spinosa", converter.mapValue(null, "isp", "sUBf. sPinosa"));
    }

    @Test
    public void testConvertsSspToSubsp()
    {
        assertMap("subsp.", "spinosa", converter.mapValue(null, "isp", "ssp spinosa"));
        assertMap("subsp.", "spinosa", converter.mapValue(null, "isp", "ssp. spinosa"));
        assertMap("subsp.", "spinosa", converter.mapValue(null, "isp", "SSP spinosa"));
        assertMap("subsp.", "spinosa", converter.mapValue(null, "isp", "Ssp. spinosa"));
    }

    @Test
    public void testConvertsFormToF()
    {
        assertMap("f.", "spinosa", converter.mapValue(null, "isp", "form spinosa"));
        assertMap("f.", "spinosa", converter.mapValue(null, "isp", "form. spinosa"));
        assertMap("f.", "spinosa", converter.mapValue(null, "isp", "FORM spinosa"));
        assertMap("f.", "spinosa", converter.mapValue(null, "isp", "Form. spinosa"));
    }

    @Test
    public void testRejectsUnknownTypes()
    {
        assertMap("", "", converter.mapValue(null, "isp", "varf spinosa"));
        assertMap("", "", converter.mapValue(null, "isp", "sspk spinosa"));
        assertMap("", "", converter.mapValue(null, "isp", "j spinosa"));
    }

    private void assertMap(String isprk, String isp, Map<String, String> result)
    {
        assertEquals(isprk, result.get("isprk"));
        assertEquals(isp, result.get("isp"));
    }
}
