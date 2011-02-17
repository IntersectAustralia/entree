/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: FailureScreenFactory.java 79 2010-06-25 02:20:40Z ryan $
 */
package au.org.intersect.entreeui.ui.wizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @version $Rev: 79 $
 *
 */
public interface FailureScreenFactory
{
    /**
     * creates a customised IWizardPage for the particular error
     */
    IWizardPage getFailureScreen(Throwable t, IWizard w);
}
