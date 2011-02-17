/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: DateUtil.java 192 2010-07-22 01:16:44Z ryan $
 */
package au.org.intersect.entreeui.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version $Rev: 192 $
 *
 */
public final class DateUtil
{
    public static String formatToday()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        Date today = new Date();
        return formatter.format(today);
    }
}
