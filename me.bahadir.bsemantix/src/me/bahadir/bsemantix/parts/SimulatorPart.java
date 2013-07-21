package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;

public class SimulatorPart {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public SimulatorPart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		NodeMeta nm = new NodeMeta(parent, SWT.None);
		formToolkit.paintBordersFor(nm);
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
