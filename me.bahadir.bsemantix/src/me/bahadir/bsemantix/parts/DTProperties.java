package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import me.bahadir.bsemantix.ngraph.dtree.Question;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Text;

public class DTProperties {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Form frmDecisionTreeEditor;
	private Section sctnPro;
	private QuestionEditor qe;
	
	

	public DTProperties() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		
		frmDecisionTreeEditor = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmDecisionTreeEditor);
		frmDecisionTreeEditor.setText("Decision Tree Editor");
		frmDecisionTreeEditor.getBody().setLayout(new GridLayout(1, false));
		
		sctnPro = formToolkit.createSection(frmDecisionTreeEditor.getBody(), Section.TITLE_BAR);
		GridData gd_sctnPro = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_sctnPro.widthHint = 176;
		sctnPro.setLayoutData(gd_sctnPro);
		formToolkit.paintBordersFor(sctnPro);
		sctnPro.setText("Pro");
		sctnPro.setExpanded(true);
		
		qe = new QuestionEditor(sctnPro, SWT.None);
		formToolkit.paintBordersFor(qe);
		sctnPro.setClient(qe);
		
	

	}

	@Inject @Optional
	void itemSelected(@UIEventTopic(DecisionEditorPart.TOPIC_TREE_ITEM_SELECTED) Object item) {
		qe.setVisible(false);
		if(item == null) {
			sctnPro.setVisible(false);
		} else {
			sctnPro.setVisible(true);
			if(item instanceof Question) {
				sctnPro.setText("Question Properties");
				qe.setQuestionData(((Question) item).getQuestionData());
				qe.setVisible(true);
				
			}
		}
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
