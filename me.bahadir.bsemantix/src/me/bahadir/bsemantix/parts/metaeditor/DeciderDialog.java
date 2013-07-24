package me.bahadir.bsemantix.parts.metaeditor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.wb.swt.SWTResourceManager;

public class DeciderDialog extends FormDialog {



	public DeciderDialog(Shell shell) {
		super(shell);
		setHelpAvailable(false);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Empty FormPage");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		
		FormText txtQuestion = managedForm.getToolkit().createFormText(managedForm.getForm().getBody(), false);
		txtQuestion.marginHeight = 10;
		txtQuestion.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtQuestion.marginWidth = 10;
		txtQuestion.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		managedForm.getToolkit().paintBordersFor(txtQuestion);
		txtQuestion.setText("New FormText", false, false);
		
		Group group = new Group(managedForm.getForm().getBody(), SWT.NONE);
		GridLayout gl_group = new GridLayout(1, false);
		gl_group.verticalSpacing = 10;
		group.setLayout(gl_group);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		managedForm.getToolkit().adapt(group);
		managedForm.getToolkit().paintBordersFor(group);
		
		Button btnRadioButton = new Button(group, SWT.RADIO);
		btnRadioButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		managedForm.getToolkit().adapt(btnRadioButton, true, true);
		btnRadioButton.setText("Radio Button");
		
		Button btnRadioButton_1 = new Button(group, SWT.RADIO);
		btnRadioButton_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		managedForm.getToolkit().adapt(btnRadioButton_1, true, true);
		btnRadioButton_1.setText("Radio Button");
	}
}
