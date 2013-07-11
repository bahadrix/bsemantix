package me.bahadir.bsemantix.ngraph.dtree;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import me.bahadir.bsemantix.S;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.zest.core.widgets.GraphNode;

public class Question extends GraphNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7522215234845313353L;
	private String text;
	private String shortText;
	
	
	public static class QuestionData {
		private String text;
		private String shortText;
		private Point location;
		public QuestionData(String text, String shortText) {
			this.text = text; 
			this.shortText = shortText;
		}
		public final String getText() {
			return text;
		}
		public final String getShortText() {
			return  shortText;
		}
		
		
	}
	
	public String getXMLStartTag() {
		return String.format("<question text=\"%s\" shortText=\"%s\" locationX=\"%d\" locationY=\"%d\">"
				,S.xmlize(qData.text), S.xmlize(qData.shortText), getLocation().x, getLocation().y);
	}
	
//	public static createFromData(QuestionData qData) {
//		
//	}
	

	
	private QuestionData qData;
	private DecisionTree decisionTree;
	
	public Question(DecisionTree decisionTree, QuestionData qData) {
		super(decisionTree, SWT.None, qData.shortText);
		setMyStyle();
		this.qData = qData;
		this.decisionTree = decisionTree;
	}

	public List<GraphNode> getChildren() {
		return decisionTree.getNodeChildren(this);
	}
	
	public QuestionData getQuestionData() {
		
		this.qData.location = this.getLocation();
		
		return qData;
	}
	
	private void setMyStyle() {
		setForegroundColor(S.SWTColor(255, 255, 255));
	    setBackgroundColor(S.SWTColor(7, 114, 161));
	    setHighlightColor(S.SWTColor(92, 204, 204));
	    setBorderHighlightColor(S.SWTColor(51, 204, 204));
	    setBorderColor(S.SWTColor(51, 204, 204));
	}
	
}
