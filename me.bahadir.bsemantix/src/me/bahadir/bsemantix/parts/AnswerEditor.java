package me.bahadir.bsemantix.parts;

import me.bahadir.bsemantix.ngraph.dtree.Answer.AnswerData;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class AnswerEditor extends Composite {
	private DataBindingContext m_bindingContext;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtText;
	private Text txtSynonym;
	
	private AnswerData answerData = new AnswerData("", "");
	private List list;
	private Text txtFact;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AnswerEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Section sctnNewSection = formToolkit.createSection(this, Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Answer Properties");
		sctnNewSection.setExpanded(true);
		
		Composite composite = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		sctnNewSection.setClient(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblText = formToolkit.createLabel(composite, "Text", SWT.NONE);
		lblText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtText = formToolkit.createText(composite, "New Text", SWT.NONE);
		txtText.setText("");
		txtText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Label lblFact = new Label(composite, SWT.NONE);
		lblFact.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblFact, true, true);
		lblFact.setText("Fact");
		
		txtFact = new Text(composite, SWT.BORDER);
		txtFact.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtFact, true, true);
		new Label(composite, SWT.NONE);
		
		Label lblSynoynms = formToolkit.createLabel(composite, "Synoynms", SWT.NONE);
		lblSynoynms.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtSynonym = formToolkit.createText(composite, "", SWT.NONE);
		txtSynonym.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if(e.detail == SWT.TRAVERSE_RETURN) {
					addSynonym();
				}
			}
		});
		
				txtSynonym.setText("");
				txtSynonym.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Button btnNewButton = formToolkit.createButton(composite, "Add", SWT.NONE);
				new Label(composite, SWT.NONE);
				
				list = new List(composite, SWT.BORDER);
				list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
				formToolkit.adapt(list, true, true);
				new Label(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addSynonym();
			}
		});
		m_bindingContext = initDataBindings();

	}

	protected void addSynonym() {
		list.add(txtSynonym.getText());
		txtSynonym.setText("");
		m_bindingContext.updateModels();
	}
	
	public AnswerData getAnswerData() {
		return answerData;
	}



	public void setAnswerData(AnswerData answerData) {
		this.answerData = answerData;
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
		IObservableList itemsListObserveWidget = WidgetProperties.items().observe(list);
		IObservableList synonymsAnswerDataObserveList = PojoProperties.list("synonyms").observe(answerData);
		bindingContext.bindList(itemsListObserveWidget, synonymsAnswerDataObserveList, null, null);
		//
		IObservableValue observeTextTxtTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtText);
		IObservableValue textAnswerDataObserveValue = PojoProperties.value("text").observe(answerData);
		bindingContext.bindValue(observeTextTxtTextObserveWidget, textAnswerDataObserveValue, null, null);
		//
		IObservableValue observeTextTxtFactObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtFact);
		IObservableValue factAnswerDataObserveValue = PojoProperties.value("fact").observe(answerData);
		bindingContext.bindValue(observeTextTxtFactObserveWidget, factAnswerDataObserveValue, null, null);
		//
		return bindingContext;
	}
}
