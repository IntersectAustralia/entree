/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: TestButtonFactory.java 244 2010-09-10 03:53:57Z ryan $
 */
package au.org.intersect.entreeui.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import au.org.intersect.entreecore.worker.OperationCancelledException;
import au.org.intersect.entreeui.worker.ExtractOnlyWorker;

/**
 * @version $Rev: 244 $
 * 
 */
public class TestButtonFactory
{
    private static final Logger LOG = Logger.getLogger(TestButtonFactory.class);

    private static final int POPUP_WIDTH = 250;
    private static final int POPUP_HEIGHT = 100;

    public static Button createTestButton(Composite composite, ExtractOnlyWorker extractWorker)
    {
        Button button = new Button(composite, SWT.NONE);

        button.setText("Test");

        button.addSelectionListener(new TestButtonSelectionListener(extractWorker, button.getShell()));

        return button;
    }

    /**
     * @version $Rev: 244 $
     * 
     */
    private static class TestButtonSelectionListener implements SelectionListener
    {
        private final ExtractOnlyWorker extractWorker;

        private final Shell shell;

        private boolean isDone;

        private Label label;

        private ProgressBarListener progressBarListener;

        private Shell dialog;

        private Composite composite;

        public TestButtonSelectionListener(ExtractOnlyWorker extractWorker, Shell shell)
        {
            this.extractWorker = extractWorker;
            this.shell = shell;
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent event)
        {
            this.widgetSelected(event);
        }

        @Override
        public void widgetSelected(SelectionEvent event)
        {
            dialog = setupDialog();
            composite = setupComposite(dialog);

            ProgressBar progressBar = new ProgressBar(composite, SWT.HORIZONTAL);
            this.label = new Label(composite, SWT.NONE);
            label.setText("Processing...");
            this.progressBarListener = setupProgressBarListener(progressBar);
            this.isDone = false;
            dialog.addListener(SWT.Close, new Listener()
            {
                @Override
                public void handleEvent(Event event)
                {
                    MessageBox mb = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL);
                    mb.setText("Are you sure you want to cancel?");
                    mb.setMessage("Click ok to cancel the test operation");
                    event.doit = TestButtonSelectionListener.this.isDone || mb.open() == SWT.OK;
                    if (event.doit)
                    {
                        extractWorker.requestCancel();
                    }
                }
            });
            startExtractInNewThread();
            dialog.open();
        }

        private ProgressBarListener setupProgressBarListener(ProgressBar progressBar)
        {
            ProgressBarListener progressBarListener = new ProgressBarListener();
            progressBarListener.setProgressBar(progressBar);
            progressBarListener.setDisplay(shell.getDisplay());
            return progressBarListener;
        }

        private Composite setupComposite(final Shell dialog)
        {
            Composite composite = new Composite(dialog, SWT.NONE);
            GridLayout gridLayout = new GridLayout();
            composite.setLayout(gridLayout);
            return composite;
        }

        private Shell setupDialog()
        {
            Shell dialog = new Shell(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
            dialog.setLayout(new GridLayout());

            dialog.setText("testing");
            dialog.setSize(POPUP_WIDTH, POPUP_HEIGHT);
            return dialog;
        }

        private void startExtractInNewThread()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        extractWorker.doWork(progressBarListener, null);
                        Display.getDefault().asyncExec(new Runnable()
                        {
                            public void run()
                            {
                                dialog.setText("done");
                                label.setText("File is at: " + extractWorker.getFilePath());
                                composite.layout();
                            }
                        });
                    }
                    catch (OperationCancelledException e)
                    {
                        // ignore
                    }
                    catch (Throwable t)
                    {
                        LOG.error("test extraction failed", t);
                        Display.getDefault().asyncExec(new Runnable()
                        {
                            public void run()
                            {
                                dialog.setText("Sorry, an error occurred.");
                                label.setText("Extraction failed");
                                composite.layout();
                            }
                        });
                    }
                    TestButtonSelectionListener.this.isDone = true;
                }
            }, "test extraction").start();
        }
    }
}
