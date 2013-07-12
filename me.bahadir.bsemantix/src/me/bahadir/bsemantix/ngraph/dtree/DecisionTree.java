package me.bahadir.bsemantix.ngraph.dtree;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.dtree.Answer.AnswerData;
import me.bahadir.bsemantix.ngraph.dtree.Leaf.LeafData;
import me.bahadir.bsemantix.ngraph.dtree.Leaf.LeafType;
import me.bahadir.bsemantix.ngraph.dtree.Question.QuestionData;
import me.bahadir.bsemantix.parts.DecisionEditorPart;

import org.apache.jena.atlas.logging.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@XmlRootElement(name="decisiontree")
public class DecisionTree extends Graph{

	protected static Logger log = Logger.getLogger(DecisionTree.class.getSimpleName());

	public static enum NodeType {QUESTION, LEAF};
	public static enum NodeStylePreset{BLUE, ORANGE}
	private final Map<String, Leaf> outputs;
	
	private boolean singleTarget = false;

	@XmlRootElement(name="decisiontree")
	public static class DecisionTreeData extends JAXNode {
		
		@XmlAttribute private String sourceUri;
		
		@XmlElementWrapper(name="leafuris")
		@XmlElement(name="leafuri")	private List<String> leafUris;
		
		
		@XmlElement(name="question") private QuestionData rootQuestionData;
		
		public DecisionTreeData() {};
		
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
			outputs.put(leafUri, new Leaf(this, new LeafData(leaf.getOntClass().getURI(), leaf.getName()))); // create possible leaf nodes
		}
		setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}

	public void loadData(DecisionTreeData decData) {
		this.decData = decData;
		if(decData.rootQuestionData != null) {
			loadQuestionRoot(decData.rootQuestionData);
		}
	}
	
	public void disposeChildren() {
		// Dispose connections
		while (getConnections().size() > 0) {
			((GraphConnection) getConnections().get(0)).dispose();
		}

		// Dispose nodes
		while (getNodes().size() > 0) {
			((GraphNode) getNodes().get(0)).dispose();
		}
	}
	
	private Question loadQuestionRoot(QuestionData qData) {
		Question q = new Question(this, qData);
		System.out.println(qData.getAnswerDatas().toString());
		for(AnswerData aData : qData.getAnswerDatas()) {
			
			System.out.println(aData);
			
			GraphNode target;
			if(aData.getTargetQuestion() != null) {
				target = loadQuestionRoot(aData.getTargetQuestion()); //recurse
				
				
			} else {
				target = new Leaf(this, aData.getTargetLeaf());
			}
			
			new Answer(this, q, target, aData);
		}
		
		return q;
	}
	
	public DecisionTreeData getDecisionTreeData() {
		decData.rootQuestionData = ((Question)findRoot()).getQuestionData(true);
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
	
	public void saveXML(OutputStream output) {

		getDecisionTreeData().asDOMElement(output);

	}
	public GraphNode findRoot() {
		for(Object obj : getNodes()) {
			if(obj instanceof Question) {
				//GraphNode node = (GraphNode) obj;
				Question q = (Question) obj;
				return q;
				
			}
		}
		return null;
	}
	
	
	public GraphConnection getDirectedConnection(GraphNode source, GraphNode target) {
		
		for(Object obj : getConnections()) {

			if(obj instanceof GraphConnection) {
				GraphConnection conn = (GraphConnection) obj;

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
	
	
	
	public Answer addAnswer(Question source, GraphNode target, AnswerData aData) {

		return new Answer(this, source, target, aData);
	}
	
	public Leaf createBlockLeaf() {
		return Leaf.createBlockLeaf(this);
	}

	
}
