/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: WorkFailedException.java 88 2010-06-28 03:16:43Z ryan $
 */
package au.org.intersect.entreecore.worker;

/**
 * @version $Rev: 88 $
 *
 */
public class WorkFailedException extends Exception
{

    private static final long serialVersionUID = 1L;
    
    private final String displayMessage;
    
    public WorkFailedException(String failureMessage, Exception e)
    {
        super(failureMessage, e);
        
        this.displayMessage = failureMessage == null ? "An unexpected error occurred." : failureMessage;
    }
    
    /**
     * @return the message to be displayed
     */
    public String getDisplayMessage()
    {
        return displayMessage;
    }
}
