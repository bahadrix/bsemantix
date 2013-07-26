package me.bahadir.bsemantix.parts.metaeditor;

import me.bahadir.bsemantix.ngraph.dtree.Answer.AnswerData;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.swt.layout.FillLayout;

public class CAnswerItem extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private final AnswerData answerData;
	
	public static interface AnswerSelectionListener {
		public void onAswerSelected(AnswerData answerData);
	}
	private AnswerSelectionListener answerListener;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CAnswerItem(Composite parent, AnswerData answerData) {
		super(parent, SWT.None);
		
		if(answerData == null) {
			answerData = new AnswerData("<answer>", "fact");
		}
		this.answerData = answerData;
		
		
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);
		
		ImageHyperlink hLink = toolkit.createImageHyperlink(this, SWT.NO_FOCUS);
		hLink.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent e) {
				if(answerListener != null) answerListener.onAswerSelected(CAnswerItem.this.answerData);
			}
			public void linkEntered(HyperlinkEvent e) {
			}
			public void linkExited(HyperlinkEvent e) {
			}
		});
		hLink.setHoverImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/badge-circle-check-24-ns.png"));
		hLink.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		hLink.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/badge-circle-direction-right-24-ns.png"));
		toolkit.paintBordersFor(hLink);
		hLink.setText(answerData.getText());
		//hLink.setToolTipText(answerData.getFact());
		

	}

	public void setAnswerListener(AnswerSelectionListener answerListener) {
		this.answerListener = answerListener;
	}
	

}
