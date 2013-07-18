package me.bahadir.bsemantix.parts;

import me.bahadir.bsemantix.ngraph.dtree.Question.QuestionData;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class QuestionEditor extends Composite {
	private DataBindingContext m_bindingContext;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtText;
	private Text txtShortText;
	private QuestionData questionData = new QuestionData();
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QuestionEditor(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Section sctnNewSection = formToolkit.createSection(this, Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Question Properties");
		sctnNewSection.setExpanded(true);
		
		Composite composite = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		sctnNewSection.setClient(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(3, false));
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel = formToolkit.createLabel(composite, "Question Text", SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtText = formToolkit.createText(composite, "New Text", SWT.NONE);
		txtText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_1 = formToolkit.createLabel(composite, "Short Text", SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtShortText = formToolkit.createText(composite, "New Text", SWT.NONE);
		txtShortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_bindingContext = initDataBindings();
		
	}

	
	
	

	public QuestionData getQuestionData() {
		return questionData;
	}

	public void setQuestionData(QuestionData questionData) {
		this.questionData = questionData;
		rebind();
	}

	public void rebind() {
		m_bindingContext.dispose();
		m_bindingContext = initDataBindings();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtText);
		IObservableValue textQuestionDataObserveValue = PojoProperties.value("text").observe(questionData);
		bindingContext.bindValue(observeTextTxtTextObserveWidget, textQuestionDataObserveValue, null, null);
		//
		IObservableValue observeTextTxtShortTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtShortText);
		IObservableValue shortTextQuestionDataObserveValue = PojoProperties.value("shortText").observe(questionData);
		bindingContext.bindValue(observeTextTxtShortTextObserveWidget, shortTextQuestionDataObserveValue, null, null);
		//
		return bindingContext;
	}
}
