/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FailureScreenFactoryImpl.java 202 2010-07-28 22:54:45Z georgina $
 */
package au.org.intersect.entreeui.ui.wizard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @version $Rev: 202 $
 *
 */
public class FailureScreenFactoryImpl implements FailureScreenFactory
{
    private Map<Throwable, IWizardPage> failureScreens = new HashMap<Throwable, IWizardPage>();

    public IWizardPage getFailureScreen(Throwable t, IWizard w)
    {
        if (!failureScreens.containsKey(t))
        {
            failureScreens.put(t, makeFailureScreen(t, w));
        }
        return failureScreens.get(t);
    }

    private IWizardPage makeFailureScreen(Throwable t, IWizard w)
    {
        return new DefaultFailureScreen(t, w);
    }
}
