package me.bahadir.bsemantix.ngraph.dtree;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.dtree.Answer.AnswerData;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.zest.core.widgets.GraphNode;
import org.w3c.dom.Element;

public class Question extends GraphNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7522215234845313353L;
	
	@XmlRootElement(name="question")
	public static class QuestionData extends JAXNode{
		
		@XmlAttribute private String text = "";
		@XmlAttribute private String shortText = "";
		@XmlAttribute private int locationX = 0;
		@XmlAttribute private int locationY = 0;
		
		@XmlElement(name="answer") private List<AnswerData> answerDatas;
		
		private Question question = null;
		
		public QuestionData() {};
		
		public QuestionData(String text, String shortText) {
			this.answerDatas = new LinkedList<>();
			this.text = text; 
			this.shortText = shortText;
		}
		public final String getText() {
			return text;
		}
		public final String getShortText() {
			return  shortText;
		}

		public List<AnswerData> getAnswerDatas() {
			return answerDatas;
		}

		public int getLocationX() {
			return locationX;
		}

		public void setLocationX(int locationX) {
			this.locationX = locationX;
		}

		public int getLocationY() {
			return locationY;
		}

		public void setLocationY(int locationY) {
			this.locationY = locationY;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void setShortText(String shortText) {
			this.shortText = shortText;
			if(question != null)
				question.setText(shortText);
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
		this.qData.question = this;
		this.decisionTree = decisionTree;
		this.setLocation(qData.locationX, qData.locationY);
		
	}

	public List<GraphNode> getChildren() {
		return decisionTree.getNodeChildren(this);
	}
	
	public QuestionData getQuestionData() {
		return getQuestionData(false);
	}
	
	public QuestionData getQuestionData(boolean refresh) {
		if(!refresh) return qData;
		qData.locationX = this.getLocation().x;
		qData.locationY = this.getLocation().y;
		qData.answerDatas.clear();
		for(int i = 0; i < getSourceConnections().size(); i++) {
			Object obj = getSourceConnections().get(i);
			if(obj instanceof Answer) {
				Answer a = (Answer) obj;
				qData.answerDatas.add(a.getAnswerData());
			}
		}
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
