package me.bahadir.bsemantix.parts;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;

public class DecisionDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DecisionDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmNewForm = formToolkit.createForm(shell);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText("New Form");
		frmNewForm.getBody().setLayout(new FormLayout());
		
		FormText formText = formToolkit.createFormText(frmNewForm.getBody(), false);
		FormData fd_formText = new FormData();
		fd_formText.right = new FormAttachment(0, 434);
		fd_formText.top = new FormAttachment(0, 10);
		fd_formText.left = new FormAttachment(0, 10);
		formText.setLayoutData(fd_formText);
		formToolkit.paintBordersFor(formText);
		formText.setText("New FormText", false, false);
		
		Button btnNewButton = new Button(frmNewForm.getBody(), SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(formText, 171);
		fd_btnNewButton.bottom = new FormAttachment(100, -10);
		btnNewButton.setLayoutData(fd_btnNewButton);
		formToolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("Previous");
		
		Button btnNext = new Button(frmNewForm.getBody(), SWT.NONE);
		fd_btnNewButton.right = new FormAttachment(btnNext, -6);
		FormData fd_btnNext = new FormData();
		fd_btnNext.bottom = new FormAttachment(btnNewButton, 0, SWT.BOTTOM);
		fd_btnNext.top = new FormAttachment(formText, 171);
		fd_btnNext.left = new FormAttachment(0, 377);
		fd_btnNext.right = new FormAttachment(100, -10);
		btnNext.setLayoutData(fd_btnNext);
		formToolkit.adapt(btnNext, true, true);
		btnNext.setText("Next");
		
		Button btnReset = new Button(frmNewForm.getBody(), SWT.NONE);
		btnReset.setText("Reset");
		FormData fd_btnReset = new FormData();
		fd_btnReset.top = new FormAttachment(formText, 171);
		fd_btnReset.bottom = new FormAttachment(100, -10);
		fd_btnReset.right = new FormAttachment(0, 67);
		fd_btnReset.left = new FormAttachment(0, 10);
		btnReset.setLayoutData(fd_btnReset);
		formToolkit.adapt(btnReset, true, true);
		
		Button button = new Button(frmNewForm.getBody(), SWT.NONE);
		button.setText("Previous");
		FormData fd_button = new FormData();
		fd_button.top = new FormAttachment(formText, 171);
		fd_button.bottom = new FormAttachment(100, -10);
		fd_button.left = new FormAttachment(btnReset, 6);
		button.setLayoutData(fd_button);
		formToolkit.adapt(button, true, true);

	}
}
