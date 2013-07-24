package me.bahadir.bsemantix.parts.metaeditor;

import org.eclipse.jface.wizard.Wizard;

public class DeciderWizard extends Wizard {

	public DeciderWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
