package me.bahadir.bsemantix.ngraph.dtree;

import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

public abstract class DecisionNode extends GraphNode {

	private Question motherQuestion;
	
	public DecisionNode(IContainer graphModel, int style) {
		super(graphModel, style);
		// TODO Auto-generated constructor stub
	}

	
	
	public DecisionNode(IContainer graphModel, int style, Object data) {
		super(graphModel, style, data);
		// TODO Auto-generated constructor stub
	}



	public DecisionNode(IContainer graphModel, int style, String text, Image image, Object data) {
		super(graphModel, style, text, image, data);
		// TODO Auto-generated constructor stub
	}



	public DecisionNode(IContainer graphModel, int style, String text, Image image) {
		super(graphModel, style, text, image);
		// TODO Auto-generated constructor stub
	}



	public DecisionNode(IContainer graphModel, int style, String text, Object data) {
		super(graphModel, style, text, data);
		// TODO Auto-generated constructor stub
	}



	public DecisionNode(IContainer graphModel, int style, String text) {
		super(graphModel, style, text);
		// TODO Auto-generated constructor stub
	}



	public Question getMotherQuestion() {
		return motherQuestion;
	}

	public void setMotherQuestion(Question motherQuestion) {
		this.motherQuestion = motherQuestion;
	}
	
	

}
