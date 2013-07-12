package me.bahadir.bsemantix.ngraph.dtree;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


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
			outputs.put(leafUri, new Leaf(this, new LeafData(leaf.getOntClass().getURI(), leaf.getName()))); // create possible leaf nodes
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
	
	public void saveXML(OutputStream output) {
		try {
			Question root = (Question) findRoot();
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element wrapper = doc.createElement("decisiontree");
			
			wrapper.setAttribute("source", getSourceUri());
			
			
			wrapper.appendChild(recurseTag(root,doc));
			doc.appendChild(wrapper);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			//StreamResult result = new StreamResult(new File("C:\\Users\\vaýo\\Desktop\\test.xml"));
			StreamResult result = new StreamResult(output);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public Element recurseTag(GraphNode root, Document doc) {
		List<GraphConnection> sourceConns = root.getSourceConnections();
		
		Element el = null;
		
//		if(root instanceof Question) {
//			Question q = (Question) root;
////			startTag = q.getXMLStartTag();
////			endTag = "</question>";
//			el = doc.createElement("question");
//			el.setAttribute("text", q.getQuestionData().getText());
//			el.setAttribute("shorttext", q.getQuestionData().getShortText());
//			el.setAttribute("locationX", String.valueOf(q.getLocation().x));
//			el.setAttribute("locationY", String.valueOf(q.getLocation().y));
//		} else if(root instanceof Leaf) {
//			Leaf l = (Leaf) root;
//			el = doc.createElement("leaf");
//			el.setAttribute("uri", l.getLeafData().type == LeafType.BLOCK ? "block" : l.getLeafData().outputUri);
//			el.setAttribute("text", l.getLeafData().text);
//			el.setAttribute("locationX", String.valueOf(l.getLeafData().));
//			el.setAttribute("locationY", String.valueOf(l.getLeafData().location.y));
////			startTag = l.getXMLStartTag();
////			endTag = "</leaf>";
//		} else {
//			return el;
//		}
		

		
		
		for(GraphConnection conn : sourceConns) {
			if(conn instanceof Answer) {
				Answer a = (Answer) conn;
				Element eAnswer = doc.createElement("answer");
				
				eAnswer.setAttribute("text", a.getAnswerData().getText());
				eAnswer.setAttribute("fact", a.getAnswerData().getFact());
				eAnswer.setAttribute("synonyms", a.getAnswerData().getSynonyms().toString());
				eAnswer.appendChild(recurseTag(conn.getDestination(), doc));
				el.appendChild(eAnswer);
				//childCode += recurseTag(conn.getDestination());
			}
		}

		
	

		return el;
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
