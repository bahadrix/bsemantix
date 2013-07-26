package me.bahadir.bsemantix.parts.metaeditor;

import java.util.LinkedList;
import java.util.List;

import me.bahadir.bsemantix.ngraph.dtree.Answer.AnswerData;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree.DecisionTreeData;
import me.bahadir.bsemantix.ngraph.dtree.Leaf.LeafData;
import me.bahadir.bsemantix.ngraph.dtree.Question;
import me.bahadir.bsemantix.ngraph.dtree.Question.QuestionData;
import me.bahadir.bsemantix.parts.metaeditor.CAnswerItem.AnswerSelectionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.events.HyperlinkEvent;

import com.sun.j3d.utils.behaviors.vp.WandViewBehavior.ResetViewListener;

public class DecisionDialog extends FormDialog {

	

	private List<CAnswerItem> answerItems;
	private Composite cAnswers;
	private IManagedForm managedForm;
	private StyledText questionText;
	private Button okButton;
	private LeafData leafData;
	private ImageHyperlink backLink;
	private QuestionData previousQuestion;
	private Composite composite;
	private Label lblNewLabel;
	private DecisionTreeData treeData;
	public DecisionDialog(Shell shell, DecisionTreeData treeData) {
		super(shell);
		this.treeData = treeData;
		this.answerItems = new LinkedList<>();
		
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Empty FormPage");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		
		composite = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(composite);
		composite.setLayout(new GridLayout(2, false));
		
		lblNewLabel = managedForm.getToolkit().createLabel(composite, "Soru", SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		backLink = managedForm.getToolkit().createImageHyperlink(composite, SWT.NONE);
		backLink.setSize(60, 26);
		backLink.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent e) {
				if(previousQuestion != null){
					loadQuestion(previousQuestion);
					
				}
			}
			public void linkEntered(HyperlinkEvent e) {
			}
			public void linkExited(HyperlinkEvent e) {
			}
		});
		backLink.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/undo.png"));
		managedForm.getToolkit().paintBordersFor(backLink);
		backLink.setText("Geri");
		
		questionText = new StyledText(managedForm.getForm().getBody(), SWT.READ_ONLY | SWT.WRAP);
		questionText.setWordWrap(true);
		questionText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		questionText.setTopMargin(5);
		questionText.setBottomMargin(5);
		questionText.setRightMargin(5);
		questionText.setLeftMargin(5);
		questionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		questionText.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				DecisionDialog.this.cAnswers.setFocus();
				
			}
		});
		managedForm.getToolkit().adapt(questionText);
		managedForm.getToolkit().paintBordersFor(questionText);
		
		cAnswers = new Composite(managedForm.getForm().getBody(), SWT.NONE);
		cAnswers.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		managedForm.getToolkit().adapt(cAnswers);
		managedForm.getToolkit().paintBordersFor(cAnswers);
		cAnswers.setLayout(new GridLayout(1, false));
		
		loadQuestion(treeData.getRootQuestionData());
		
		
	}
	
	private void loadQuestion(QuestionData qData) {
	
		
		reset();
		backLink.setVisible(!qData.equals(treeData.getRootQuestionData()));
		
		if(okButton != null) okButton.setEnabled(false);
		questionText.setText(qData.getText());
		//questionText.setWordWrap(true);
		
		for(AnswerData a : qData.getAnswerDatas()) {
		
			CAnswerItem answerItem = new CAnswerItem(cAnswers, a);
			answerItem.setAnswerListener(new AnswerSelectionListener() {
				
				@Override
				public void onAswerSelected(AnswerData answerData) {
					setPreviousQuestion(answerData.getSourceQuestionData());
					if(answerData.getTargetQuestion() != null) {
						loadQuestion(answerData.getTargetQuestion());
					} else {
						loadResult(answerData.getTargetLeaf());
					}
					
				}
			});
			
			managedForm.getToolkit().adapt(answerItem);
			managedForm.getToolkit().paintBordersFor(answerItem);
			answerItems.add(answerItem);
			
		}
		managedForm.getForm().getBody().layout(true, true);
	}
	
	
	private void reset() {
		clearAnswers();
		
		backLink.setVisible(previousQuestion != null);
	}

	private void setPreviousQuestion(QuestionData previousQuestion) {
		this.previousQuestion = previousQuestion;
	}

	private void loadResult(LeafData leafData) {
		reset();
		questionText.setText(leafData.getText());
		this.leafData = leafData;
		okButton.setEnabled(true);
	}
	
	private void clearAnswers() {
		while(answerItems.size() > 0) {
			answerItems.get(0).dispose();
			answerItems.remove(0);
		}
	}

	
	
	public LeafData getLeafData() {
		return leafData;
	}

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		// TODO Auto-generated method stub
		Button button = super.createButton(parent, id, label, defaultButton);
		
		if(id==OK) {
			okButton = button;
			button.setEnabled(false);
		}
		return button;
	}

	
	
	
}
