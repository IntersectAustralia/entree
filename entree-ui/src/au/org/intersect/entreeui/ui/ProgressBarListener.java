/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: ProgressBarListener.java 213 2010-07-30 05:06:35Z georgina $
 */
package au.org.intersect.entreeui.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import au.org.intersect.entreecore.worker.ProgressListener;

/**
 * ProgressBar implementation of ProgressListener
 * 
 * @version $Rev: 213 $
 * 
 */
public class ProgressBarListener implements ProgressListener
{
    private ProgressBar progressBar;
    private Display display;
    private int adjustedMax;

    public void setDisplay(Display display)
    {
        this.display = display;
    }

    public void setProgressBar(ProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }

    public void setMaxProgress(final int maxProgress)
    {
        ensureIsSetupCorrectly();
        
        // special case for setting the max to zero
        final int adjustedMax = maxProgress == 0 ? 1 : maxProgress;
        display.asyncExec(new Runnable()
        {
            public void run()
            {
                progressBar.setMaximum(adjustedMax);
            }
        });
        if (maxProgress == 0)
        {
            updateProgress(adjustedMax);
        }
        this.adjustedMax = adjustedMax;
    }

    public void updateProgress(final int currentProgress)
    {
        ensureIsSetupCorrectly();
        display.asyncExec(new Runnable()
        {
            public void run()
            {
                progressBar.setSelection(currentProgress);
            }
        });
    }
    
    public void setFinished()
    {
        updateProgress(this.adjustedMax);
    }

    private void ensureIsSetupCorrectly()
    {
        if (display == null || progressBar == null)
        {
            throw new IllegalStateException("ProgressBarListener is not set up correctly."
                    + "  Ensure its display and progressBar are set.");
        }
    }

}