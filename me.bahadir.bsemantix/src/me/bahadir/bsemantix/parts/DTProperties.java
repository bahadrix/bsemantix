package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import me.bahadir.bsemantix.ngraph.dtree.Answer;
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
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.StackLayout;

public class DTProperties extends ViewPart {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Form frmDecisionTreeEditor;

	private QuestionEditor questionEditor;
	private AnswerEditor answerEditor;
	private Composite composite;

	public DTProperties() {
	}

	void hideEditors() {
		questionEditor.setVisible(false);
		answerEditor.setVisible(false);
	}

	@Inject
	@Optional
	void itemSelected(@UIEventTopic(DecisionEditorPart.TOPIC_TREE_ITEM_SELECTED) Object item) {
		hideEditors();

		if (item == null) {
			hideEditors();
		} else {

			if (item instanceof Question) {
				questionEditor.setQuestionData(((Question) item).getQuestionData());
				questionEditor.setVisible(true);

			} else if (item instanceof Answer) {
				answerEditor.setAnswerData(((Answer) item).getAnswerData());
				answerEditor.setVisible(true);
			}

		}
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO Set the focus to control
	}

	@PostConstruct
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		frmDecisionTreeEditor = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmDecisionTreeEditor);
		frmDecisionTreeEditor.setText("Decision Tree Editor");
		frmDecisionTreeEditor.getBody().setLayout(new GridLayout(1, false));

		composite = formToolkit.createComposite(frmDecisionTreeEditor.getBody(), SWT.NONE);
		composite.setLayout(new StackLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(composite);
		
		
		answerEditor = new AnswerEditor(composite, SWT.None);
		formToolkit.paintBordersFor(answerEditor);

		questionEditor = new QuestionEditor(composite, SWT.None);
		formToolkit.paintBordersFor(questionEditor);

		hideEditors();

	}

}
