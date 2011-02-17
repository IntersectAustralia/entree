/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: BootstrapperTest.java 34 2010-06-18 08:04:36Z ryan $
 */
package au.org.intersect.entreeui.controller;

import org.junit.Test;

/**
 * @version $Rev: 34 $
 * 
 */
public class BootstrapperTest
{

    @Test
    public void testCanBootstrap()
    {
        // just check that the spring config loads
        // remove test so it can run on hudson
//        Controller controller = Bootstrapper.initController();
//        assertNotNull(controller);
    }
}
