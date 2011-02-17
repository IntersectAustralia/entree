/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: TimestampFormatterConverter.java 253 2010-09-28 01:29:52Z ryan $
 */
package au.org.intersect.entreecore.mapping;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 
 * @version $Rev: 253 $
 */
public class TimestampFormatterConverter extends SingleValueFieldConverter
{
    private static final Logger LOG = Logger.getLogger(TimestampFormatterConverter.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");;

    @Override
    protected String mapSingleValue(String accid, Object value)
    {
        return formatTimestamp(value, accid);
    }

    private String formatTimestamp(Object value, String accid)
    {
        if (!value.getClass().isAssignableFrom(Timestamp.class))
        {
            LOG.warn("Value from database is not a timestamp. Try using a different converter. Value class is "
                    + value.getClass().getName() + "for " + accid);
            return value.toString();
        }
        Timestamp timestampValue = (Timestamp) value;
        Date date = new Date(timestampValue.getTime());
        return DATE_FORMAT.format(date);
    }

}
