package me.bahadir.bsemantix.synaptix;

import java.util.ArrayList;
import java.util.List;

public class Question extends Node {
	private String text;
	private List<Answer> answers = new ArrayList<Answer>();

	
	
	public Question(String text) {
		this.text = text;
	}

	public void addAnswer(Answer answer) {
		answer.setParent(this);
		answers.add(answer);

	}
	
	
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public String getText() {
		return text;
	}
	
	
	
}