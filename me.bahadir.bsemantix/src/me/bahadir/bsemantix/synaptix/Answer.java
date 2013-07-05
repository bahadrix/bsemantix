package me.bahadir.bsemantix.synaptix;

public class Answer {
	private String text;
	private String fact;
	private Question parentQuestion;
	private Node target;

	public Answer(String text) {
		this.text = text;

	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	void setParent(Question question) {
		this.parentQuestion = question;
	}

	public Question getParent() {
		return parentQuestion;
	}

	public String getText() {
		return text;
	}

	public String getFact() {
		return fact;
	}

}