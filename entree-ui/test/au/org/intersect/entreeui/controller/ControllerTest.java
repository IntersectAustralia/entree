/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: ControllerTest.java 229 2010-08-11 23:23:33Z georgina $
 */
package au.org.intersect.entreeui.controller;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import au.org.intersect.entreecore.rowmapper.CsvWritingRowHandler;
import au.org.intersect.entreeui.ui.EntreeApplication;

/**
 * @version $Rev: 229 $
 * 
 */
public class ControllerTest
{

    @Mock
    private CsvWritingRowHandler rowHandler;

    @Mock
    private EntreeApplication entreeApplication;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testControllerStart()
    {
        Controller controller = new Controller(rowHandler, entreeApplication);
        controller.start();
        verify(entreeApplication).open();
    }

}
