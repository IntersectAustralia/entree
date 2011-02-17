/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: BasicRowMapper.java 230 2010-08-11 23:23:53Z georgina $
 */
package au.org.intersect.entreecore.rowmapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import au.org.intersect.entreecore.mapping.FieldConverter;
import au.org.intersect.entreecore.mapping.MultiValueConverter;

/**
 * Simple version of the mapper that does no special transformations
 * 
 * @version $Rev: 230 $
 */
public class BasicRowMapper extends ConfigurableRowMapper
{

    public BasicRowMapper(RowMapper<Map<String, Object>> backingMapper)
    {
        super(backingMapper, new HashMap<String, FieldConverter>(), new ArrayList<MultiValueConverter>());
    }

}
