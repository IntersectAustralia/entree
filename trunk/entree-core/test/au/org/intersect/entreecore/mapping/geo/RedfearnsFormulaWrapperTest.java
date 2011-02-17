/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: RedfearnsFormulaWrapperTest.java 235 2010-08-12 23:29:06Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * 
 * @version $Rev: 235 $
 */
public class RedfearnsFormulaWrapperTest
{

    @Mock
    private RedfearnsFormula formula;
    private RedfearnsFormulaWrapper wrapper;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        wrapper = new RedfearnsFormulaWrapper();
        wrapper.setRedfearnsFormula(formula);
    }

    @Test
    public void testCreatesInputMapCorrectlyAndParsesOutputCorrectly()
    {
        RedfearnsFormulaParameters params = RedfearnsFormulaParameters.ANS;

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        Map output = new HashMap();
        output.put("lat", "20");
        output.put("long", "40");

        when(
                formula.convertGridReference(captor.capture(), eq(params.getFlattening()),
                        eq(params.getMajorSemiAxis()), eq(params.getCentralScaleFactor()))).thenReturn(output);

        GeodeticPosition latlong = wrapper.convert("56", "123", "456", params);
        Map map = captor.getValue();

        assertEquals("56", map.get("gridref1_1"));
        assertEquals("123", map.get("gridref1_2"));
        assertEquals("S", map.get("gridref1_3"));
        assertEquals("456", map.get("gridref1_4"));
        assertEquals("S", map.get("gridref1_5"));

        assertEquals(20, latlong.lat(), 0.05);
        assertEquals(40, latlong.lon(), 0.05);

    }
}
