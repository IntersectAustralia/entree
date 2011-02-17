/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FileSendingException.java 178 2010-07-20 01:40:45Z georgina $
 */
package au.org.intersect.entreecore.filesender;

/**
 * @version $Rev: 178 $
 *
 */
public class FileSendingException extends Exception
{
    private static final long serialVersionUID = 1L;

    public FileSendingException(Throwable t)
    {
        super(t);
    }

    public FileSendingException(String message)
    {
        super(message);
    }

}
