/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: WorkFailedExceptionTest.java 123 2010-06-29 03:59:42Z ryan $
 */
package au.org.intersect.entreecore.worker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;


/**
 * @version $Rev: 123 $
 *
 */
public class WorkFailedExceptionTest
{
    @Test
    public void testDefaultMessage()
    {
        Exception cause = mock(Exception.class);
        WorkFailedException e = new WorkFailedException(null, cause);
        assertNotNull(e.getDisplayMessage());
        verifyZeroInteractions(cause);
    }
    
    @Test
    public void testSuppliedMessage()
    {
        Exception cause = mock(Exception.class);
        String displayMessage = "blah";
        WorkFailedException e = new WorkFailedException(displayMessage, cause);
        assertNotNull(e.getDisplayMessage());
        assertEquals(displayMessage, e.getDisplayMessage());
        verifyZeroInteractions(cause);
    }
    
}
