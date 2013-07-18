package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class PropertiesPart extends ViewPart {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());


	@Inject
	public PropertiesPart() {
		// TODO Your code here
		
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		
		
	}

	@Focus
	public void onFocus() {
		// TODO Your code here
	}
	@PostConstruct
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmNewForm = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText("Node Editor");
		frmNewForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Section sctnQuestion = formToolkit.createSection(frmNewForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctnQuestion);
		sctnQuestion.setText("Question");
		// TODO Auto-generated method stub
		
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}