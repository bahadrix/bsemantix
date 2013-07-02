package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.Console;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class ProgressControl implements IProgressMonitorWithBlocking {
	private ProgressBar progressBar;

	@Inject
	UISynchronize sync;

	@PostConstruct
	public void createControls(Composite parent) {
		progressBar = new ProgressBar(parent, SWT.SMOOTH);
		progressBar.setBounds(100, 10, 200, 20);
	}

	@Override
	public void worked(final int work) {
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setSelection(progressBar.getSelection() + work);
			}
		});
	}

	private void resetProgress() {

		subTask("Complete");
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setSelection(0);
			}
		});

	}

	@Override
	public void subTask(String name) {
		Console.midText("Task: " + name);
		
	}

	@Override
	public void setTaskName(String name) {

	}

	@Override
	public void setCanceled(boolean value) {

	}

	@Override
	public boolean isCanceled() {
		// resetProgress();
		return false;
	}

	@Override
	public void internalWorked(double work) {
	}

	@Override
	public void done() {
		System.out.println("Done");
		resetProgress();

	}

	@Override
	public void beginTask(final String name, final int totalWork) {
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setMaximum(totalWork);
				progressBar.setToolTipText(name);
			}
		});
		Console.println(name);
	}

	@Override
	public void setBlocked(IStatus reason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBlocked() {
		// TODO Auto-generated method stub

	}
}
