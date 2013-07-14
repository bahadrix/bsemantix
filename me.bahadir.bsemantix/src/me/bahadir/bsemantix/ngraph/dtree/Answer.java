package me.bahadir.bsemantix.ngraph.dtree;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import me.bahadir.bsemantix.ngraph.dtree.Leaf.LeafData;
import me.bahadir.bsemantix.ngraph.dtree.Question.QuestionData;

import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

public class Answer extends GraphConnection{

	@XmlRootElement(name="answer")
	public static class AnswerData extends JAXNode{
		@XmlAttribute private String text = ""; // evet
		@XmlAttribute private String fact = ""; // Madde sýcak
	 
		@XmlElementWrapper(name="synonyms")
		@XmlElement(name="synonym")	public SynonymSet synonyms;

		@XmlElement(name="question") private QuestionData targetQuestion = null;
		@XmlElement(name="leaf") private LeafData targetLeaf = null;
		
		private Answer answer = null;
		
		public AnswerData() {};
	
		public AnswerData(String text, String fact) {
			this(text,fact,new SynonymSet());
		}

		public AnswerData(String text, String fact, SynonymSet synonyms) {
			this.text = text;
			this.fact = fact;
			this.synonyms = synonyms;
		}

		public String getText() {
			return text;
		}

		public String getFact() {
			return fact;
		}

		public SynonymSet getSynonyms() {
			return synonyms;
		}

		public QuestionData getTargetQuestion() {
			return targetQuestion;
		}

		public LeafData getTargetLeaf() {
			return targetLeaf;
		}

		public void setText(String text) {
			this.text = text;
			if(answer != null) answer.setText(text);
			
		}

		public void setSynonyms(SynonymSet synonyms) {
			this.synonyms = synonyms;
		}
		
		
		
		
	}
	
	private AnswerData aData;
	private GraphNode destination;
	
	public Answer(DecisionTree decisionTree, GraphNode source, GraphNode destination, AnswerData aData) {
		super(decisionTree, ZestStyles.CONNECTIONS_DIRECTED, source, destination);
		this.aData = aData;
		this.aData.answer = this;
		this.destination = destination;
		setText(aData.text);
		
	}

	public AnswerData getAnswerData() {
		if(destination instanceof Question) {
			Question q = (Question) destination;
			aData.targetQuestion = q.getQuestionData(true);
		} else {
			Leaf l = (Leaf) destination;
			aData.targetLeaf = l.getLeafData();
		}
		return aData;
	}

	
	
	
	
}


