package me.bahadir.bsemantix.ngraph.dtree;

import java.io.Serializable;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.SphereNode;

import org.eclipse.swt.SWT;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

public class Leaf extends GraphNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2649898462705767113L;

	public enum LeafType {OUTPUT, BLOCK}
	public final LeafType type;
	public String outputUri = null;
	
	public static Leaf createBlockLeaf(DecisionTree decisionTree) {
		Leaf blockLeaf = new Leaf(decisionTree, "<block>", null);
		return blockLeaf;
	}
	
	public Leaf(DecisionTree decisionTree, String text, SphereNode output) {
		super(decisionTree, SWT.None, text);
		if(output == null) {
			this.type = LeafType.BLOCK;
		} else {
			this.type = LeafType.OUTPUT;
			this.outputUri = output.getOntClass().getURI();
		}
		setMyStyle();
	}

	public String getXMLStartTag() {
		return String.format("<leaf uri=\"%s\">",
				type == LeafType.BLOCK ? "http://bahadir.me/bsemantix#blockingleaf" : outputUri
				);
	}
	
	private void setMyStyle() {
		switch (type) {
		case OUTPUT:
			setForegroundColor(S.SWTColor(0, 0, 0));
			setBackgroundColor(S.SWTColor(147, 181, 45));
			setBorderColor(S.SWTColor(117, 157, 0));
			setHighlightColor(S.SWTColor(180, 242, 0));
			setBorderHighlightColor(S.SWTColor(147, 181, 45));
			break;
		case BLOCK:
			setForegroundColor(S.SWTColor(255, 255, 255));
			setBackgroundColor(S.SWTColor(200, 113, 48));
			setHighlightColor(S.SWTColor(255, 116, 0));
			setBorderHighlightColor(S.SWTColor(255, 150, 64));
			break;
		}

	}
}
