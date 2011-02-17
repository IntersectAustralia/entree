/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FilesystemUtil.java 71 2010-06-24 00:32:39Z ryan $
 */
package au.org.intersect.entreeui.util;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @version $Rev: 71 $
 * 
 */
public class FilesystemUtil
{
    private static final Logger LOG = Logger.getLogger(FilesystemUtil.class);

    public void ensureDirsExist(String filePath)
    {

        File file = new File(filePath);
        String dirName = new File(file.getAbsolutePath().substring(0,
                file.getAbsolutePath().length() - file.getName().length())).getAbsolutePath();
        LOG.info("attempting to create: " + dirName);
        File dirFile = new File(dirName);
        if (!dirFile.exists() && !dirFile.mkdirs())
        {
            // TODO handle properly
            throw new RuntimeException("Couldn't create " + dirName);
        }
    }

}
