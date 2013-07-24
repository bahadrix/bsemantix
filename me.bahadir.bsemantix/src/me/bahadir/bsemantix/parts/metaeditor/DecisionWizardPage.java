package me.bahadir.bsemantix.parts.metaeditor;

import me.bahadir.bsemantix.ngraph.dtree.Question;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;

public class DecisionWizardPage extends WizardPage {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Question question;
	/**
	 * Create the wizard.
	 */
	public DecisionWizardPage(Question question) {
		super("wizardPage");
		setTitle("Storage Facility Decider");
		setDescription("Details info");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Label lblNewLabel = formToolkit.createLabel(container, "Question", SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		FormText formText = formToolkit.createFormText(container, false);
		formText.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		formText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		formText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		formToolkit.paintBordersFor(formText);
		formText.setText("New FormText", false, false);
		
		Button btnRadioButton_1 = new Button(container, SWT.RADIO);
		formToolkit.adapt(btnRadioButton_1, true, true);
		btnRadioButton_1.setText("Radio Button");
		
		Button btnRadioButton = new Button(container, SWT.RADIO);
		formToolkit.adapt(btnRadioButton, true, true);
		btnRadioButton.setText("Radio Button");
		btnRadioButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		question.getChildren();
	}

}
