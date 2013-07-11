package me.bahadir.bsemantix.ngraph.dtree;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.dtree.Question.QuestionData;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;


public class DecisionTree extends Graph implements Serializable{


	private static final long serialVersionUID = 437882257184307685L;

	public static enum NodeType {QUESTION, LEAF};
	public static enum NodeStylePreset{BLUE, ORANGE}
	private final Map<String, Leaf> outputs;
	
	private boolean singleTarget = false;

	public static class DecisionTreeData {
		private final String sourceUri;
		private final List<String> leafUris;
		public DecisionTreeData(String sourceUri, List<String> leafUris) {
			this.sourceUri = sourceUri;
			this.leafUris = leafUris;
	
		}
		
	}
	
	private DecisionTreeData decData;
	
	public DecisionTree(Composite parent, NeuralGraph ng, DecisionTreeData decData) {
		super(parent, SWT.NONE);
		this.decData = decData;
		

		singleTarget =decData.leafUris.size() == 1; // if list contains single item, tree is single targeted
		outputs = new LinkedHashMap<>();
		for(String leafUri : decData.leafUris) {
			SphereNode leaf = ng.getNodeByUri(leafUri);
			outputs.put(leafUri, new Leaf(this, leaf.getName(), leaf)); // create possible leaf nodes
		}
		setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}


	
	public DecisionTreeData getDecisionTreeData() {
		return decData;
	}
	
	public List<GraphNode> getNodeChildren(GraphNode node) {
		List<GraphNode> children = new LinkedList<>();
		for(Object obj : getConnections()) {
			if(obj instanceof GraphConnection) {
				GraphConnection con = (GraphConnection) obj;
				if(con.getSource().equals(node)) {
					GraphNode child = (GraphNode) con.getDestination();
					if(!children.contains(child)) {
						children.add(child);
					}
				}
			}
		}
		return children;
	}
	
	public GraphConnection getDirectedConnection(GraphNode source, GraphNode target) {
		
		for(Object obj : getConnections()) {
			System.out.println("tar");
			if(obj instanceof GraphConnection) {
				GraphConnection conn = (GraphConnection) obj;
				System.out.println("cec");
				if(conn.getSource().equals(source) && conn.getDestination().equals(target)) {
					return conn;
				}
			} else {
				
			}
			
		}
		return null;
		
	}
	
	public boolean hasDirectedConnection(GraphNode source, GraphNode target) {
		return getDirectedConnection(source, target) != null;
	}
	
	public final Leaf getLeaf(SphereNode sn) {
		return outputs.get(sn.getOntClass().getURI());
	}
	
	

	public final String getSourceUri() {
		return decData.sourceUri;
	}

	public final Map<String, Leaf> getOutputs() {
		return outputs;
	}

	public final boolean isSingleTarget() {
		return singleTarget;
	}

	public Question addQuestion(String text, String shortText) {
		Question q = new Question(this, new QuestionData(text, shortText));
		return q;
	}

	public Question addQuestion(String text) {
		return addQuestion(text, text);
	}
	
	public Answer addAnswer(String text, Question source, GraphNode target) {
		return addAnswer(text, source, target, null);
	}
	
	public Answer addAnswer(String text, Question source, GraphNode target, String fact) {
		System.out.println(target);
		return new Answer(this, source, target, text, fact);
	}
	
	public Leaf createBlockLeaf() {
		return Leaf.createBlockLeaf(this);
	}

	
}
