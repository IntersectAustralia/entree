/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: Controller.java 229 2010-08-11 23:23:33Z georgina $
 */
package au.org.intersect.entreeui.controller;

import org.apache.log4j.Logger;


import au.org.intersect.entreecore.rowmapper.CsvWritingRowHandler;
import au.org.intersect.entreeui.ui.EntreeApplication;

/**
 * @version $Rev: 229 $
 * 
 */
public class Controller
{
    private static final Logger LOG = Logger.getLogger(Controller.class);

    private final CsvWritingRowHandler rowHandler;

    private EntreeApplication entreeApplication;

    public Controller(CsvWritingRowHandler rowHandler, EntreeApplication entreeApplication)
    {
        this.rowHandler = rowHandler;
        this.entreeApplication = entreeApplication;

        this.rowHandler.setListener(this.entreeApplication.getCsvProgressListener());
    }

    /**
     * start the wizard
     */
    public void start()
    {
        LOG.info("starting up");
        this.entreeApplication.open();
    }

}
