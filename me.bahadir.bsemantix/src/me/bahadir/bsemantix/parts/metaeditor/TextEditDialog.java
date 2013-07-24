package me.bahadir.bsemantix.parts.metaeditor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.wb.swt.SWTResourceManager;

public class TextEditDialog extends FormDialog {
	private DataBindingContext m_bindingContext;

	private String title;
	private Text text;
	private StyledText styledText;
	
	public TextEditDialog(Shell shell, String title, Text text) {
		super(shell);
		setHelpAvailable(false);
		this.text = text;
		this.title = title;
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(title);
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		
		styledText = new StyledText(managedForm.getForm().getBody(), SWT.FULL_SELECTION);
		styledText.setMarginColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		styledText.setLeftMargin(10);
		managedForm.getToolkit().adapt(styledText);
		managedForm.getToolkit().paintBordersFor(styledText);
		m_bindingContext = initDataBindings();
		m_bindingContext.updateTargets();
	}
	
	
	
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == OK) {
			m_bindingContext.updateModels();
		}
		super.buttonPressed(buttonId);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextStyledTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(styledText);
		IObservableValue textTextObserveValue = PojoProperties.value("text").observe(text);
		bindingContext.bindValue(observeTextStyledTextObserveWidget, textTextObserveValue, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST));
		//
		return bindingContext;
	}
}
