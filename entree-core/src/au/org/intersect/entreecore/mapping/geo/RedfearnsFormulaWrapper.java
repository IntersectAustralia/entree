/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: RedfearnsFormulaWrapper.java 235 2010-08-12 23:29:06Z georgina $
 */
package au.org.intersect.entreecore.mapping.geo;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @version $Rev: 235 $
 */
public class RedfearnsFormulaWrapper
{
    private RedfearnsFormula redfearnsFormula = new RedfearnsFormula();

    public GeodeticPosition convert(String zone, String eastings, String northings, RedfearnsFormulaParameters params)
    {
        Map<String, String> input = getInputMap(zone, eastings, northings);
        Map output = redfearnsFormula.convertGridReference(input, params.getFlattening(), params
                .getMajorSemiAxis(), params.getCentralScaleFactor());
        return getOutput(output);
    }

    private GeodeticPosition getOutput(Map convertGridReference)
    {
        String latitude = (String) convertGridReference.get("lat");
        String longitude = (String) convertGridReference.get("long");
        return new GeodeticPosition(Double.parseDouble(longitude), Double.parseDouble(latitude), 0);
    }

    private Map<String, String> getInputMap(String zone, String eastings, String northings)
    {
        Map<String, String> input = new HashMap<String, String>();
        input.put("gridref1_1", zone);
        input.put("gridref1_2", eastings);
        input.put("gridref1_3", "S");
        input.put("gridref1_4", northings);
        input.put("gridref1_5", "S");
        return input;
    }

    public void setRedfearnsFormula(RedfearnsFormula redfearnsFormula)
    {
        this.redfearnsFormula = redfearnsFormula;
    }

}
