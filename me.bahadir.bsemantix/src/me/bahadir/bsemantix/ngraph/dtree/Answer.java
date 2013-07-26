package me.bahadir.bsemantix.ngraph.dtree;


import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
	@XmlAccessorType(XmlAccessType.NONE)
	public static class AnswerData extends JAXNode{
		@XmlAttribute private String text = ""; // evet
		@XmlAttribute private String fact = ""; // Madde sýcak
	 
		@XmlElementWrapper(name="synonyms")
		@XmlElement(name="synonym")	public List<String> synonyms;

		@XmlElement(name="question") private QuestionData targetQuestion = null;
		@XmlElement(name="leaf") private LeafData targetLeaf = null;
		
		private Answer answer = null;
		
		public AnswerData() {};
	
		/**
		 * This one only used for decision dialog, not for hierarchy 
		 */
		private QuestionData sourceQuestionData;
		
		public AnswerData(String text, String fact) {
			this(text,fact,new LinkedList<String>());
		}

		public AnswerData(String text, String fact, List<String>  synonyms) {
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

		public List<String> getSynonyms() {

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

		public void setSynonyms(List<String> synonyms) {
			System.out.println("synset");
			this.synonyms = synonyms;
		}

		public QuestionData getSourceQuestionData() {
			return sourceQuestionData;
		}
		
		
		
		
	}
	
	private AnswerData aData;
	private GraphNode destination;
	
	public Answer(DecisionTree decisionTree, GraphNode source, GraphNode destination, AnswerData aData) {
		super(decisionTree, ZestStyles.CONNECTIONS_DIRECTED, source, destination);
		this.aData = aData;
		if(source instanceof Question) {
			aData.sourceQuestionData = ((Question) source).getQuestionData();
		}
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


