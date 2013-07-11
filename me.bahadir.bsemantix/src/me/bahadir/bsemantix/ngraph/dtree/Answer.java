package me.bahadir.bsemantix.ngraph.dtree;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

public class Answer extends GraphConnection implements Serializable{

	
	
	private static final long serialVersionUID = 2869572002230458061L;
	
		
	// Örnek soru: madde sýcak mý?
	String text; // evet
	String fact; // Madde sýcak
	public List<String> synonyms = new LinkedList<>(); // biraz, azýcýk, ýlýk (bunlarýn hepsi evet demek bu soru için) 
	
	public Answer(DecisionTree decisionTree, GraphNode source, GraphNode destination, String text, String fact) {
		super(decisionTree, ZestStyles.CONNECTIONS_DIRECTED, source, destination);
		setText(text);
		this.fact = fact;
		this.text = text;
	}
	
}


