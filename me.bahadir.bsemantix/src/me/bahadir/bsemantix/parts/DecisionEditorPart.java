 
package me.bahadir.bsemantix.parts;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree.DecisionTreeData;
import me.bahadir.bsemantix.ngraph.dtree.Answer;
import me.bahadir.bsemantix.ngraph.dtree.Leaf;
import me.bahadir.bsemantix.ngraph.dtree.Question;
import me.bahadir.bsemantix.semantic.SampleOM;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class DecisionEditorPart {
	protected static Logger log = Logger.getLogger(DecisionEditorPart.class.getSimpleName());
	private Composite treeBench;
	private DecisionTree activeTree;
	private Menu addLeafMenu;
	@Inject
	public DecisionEditorPart() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(final Composite parent) {


		parent.setLayout(new GridLayout());
		
		ToolBar bar = new ToolBar(parent, SWT.FLAT);
		
		activeTree = sampleTree(parent);
		
		
		final ToolItem tbAddLeaf = new ToolItem(bar, SWT.DROP_DOWN);
		

		tbAddLeaf.setText("Add Leaf");
		tbAddLeaf.setImage(S.getImage("icons/leaf-plus.png"));
		tbAddLeaf.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				ToolItem item = (ToolItem) e.widget;
				Rectangle rect = item.getBounds();
				Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
				addLeafMenu.setLocation(pt.x, pt.y + rect.height);
				addLeafMenu.setVisible(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final ToolItem tbAddQuestionNode = new ToolItem(bar, SWT.PUSH);
		tbAddQuestionNode.setText("Add Question");
		tbAddQuestionNode.setImage(S.getImage("icons/question.png"));
		tbAddQuestionNode.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				addQuestion();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final ToolItem tbExportXML = new ToolItem(bar, SWT.PUSH);
		tbExportXML.setText("Export");
		tbExportXML.setImage(S.getImage("icons/xml.png"));
		tbExportXML.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				exportXML();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		generateMenus(bar);
		
		bar.pack();
		
		
		
		
		
		
		
	}
	
	private void exportXML() {

		
		Question root = (Question) findRoot();
		
		log.info(recurseTag(root));

	}
	
	public GraphNode findRoot() {
		for(Object obj : activeTree.getNodes()) {
			if(obj instanceof Question) {
				//GraphNode node = (GraphNode) obj;
				Question q = (Question) obj;
				return q;
				
			}
		}
		return null;
	}
	
	public String recurseTag(GraphNode root) {
		List<GraphConnection> sourceConns = root.getSourceConnections();
		String startTag = "";
		String endTag = "";
		String code = "";
		
		if(root instanceof Question) {
			Question q = (Question) root;
			startTag = q.getXMLStartTag();
			endTag = "</question>";
		} else if(root instanceof Leaf) {
			Leaf l = (Leaf) root;
			startTag = l.getXMLStartTag();
			endTag = "</leaf>";
		}
		
		System.out.println(root.getText());
		
		String childCode = "";
		for(GraphConnection conn : sourceConns) {
			
			childCode += recurseTag(conn.getDestination());
		}

		code = startTag + childCode + endTag;
	

		return code;
	}
	
	
	
	protected void addQuestion() {
		// TODO Auto-generated method stub
		int selectSize = activeTree.getSelection().size();
		if(selectSize < 1) {
			log.warning("You must select a question node");
			return;
		}
		
		Object object = activeTree.getSelection().get(selectSize - 1);
		if(object instanceof Question) {
			Question source = (Question) object;
			Question target = activeTree.addQuestion("<question>");
			activeTree.addAnswer("<answer>", source, target);
		} else {
			log.warning("You must select a question node");
		
		}
	}
	
	

	private void addLeaf(Leaf leaf) {
		
		int selectSize = activeTree.getSelection().size();
		if(selectSize < 1) {
			log.warning("You must select a question node");
			return;
		}
		
		Object object = activeTree.getSelection().get(selectSize - 1);
		if(object instanceof Question) {
			Question source = (Question) object;
			Leaf targetLeaf = leaf == null ? activeTree.createBlockLeaf() : leaf;
			
			if(!activeTree.hasDirectedConnection(source, targetLeaf)) {
			
				activeTree.addAnswer(
						"<answer>", 
						source, 
						targetLeaf
						);
				onItemAdded();
			} else {
				log.warning("these nodes already connected");
			}
			
		} else {
			log.warning("You must select a question node");
		
		}
		//if(leaf == null) leaf = activeTree.createBlockLeaf();
		
		
	}
	
	private void onItemAdded() {
		activeTree.applyLayout();
	}
	
	private void generateMenus(ToolBar bar) {
		addLeafMenu = new Menu(bar);
		
		MenuItem item = new MenuItem(addLeafMenu, SWT.None);
		item.setText("Block leaf");
		item.setImage(S.getImage("icons/leaf-red.png"));
		item.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				addLeaf(null);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		if(activeTree.getOutputs().size() > 0) {
			MenuItem seperator = new MenuItem(addLeafMenu, SWT.SEPARATOR);
			
		}
		for(final Leaf leaf : activeTree.getOutputs().values()) {
			MenuItem leafItem = new MenuItem(addLeafMenu, SWT.None);
			leafItem.setText(leaf.getText());
			leafItem.setImage(S.getImage("icons/leaf.png"));
			leafItem.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					addLeaf(leaf);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		
	}

	
	private DecisionTree sampleTree(Composite parent) {

		OntModel om = SampleOM.getOntologyModel();
		NeuralGraph ng = new NeuralGraph();
		
		SphereNode sn1 = new SphereNode(S.unitVectorX);
			sn1.setOntClass(om.getOntClass("bx:Animal"));
		SphereNode sn2 = new SphereNode(S.unitVectorY);
			sn2.setOntClass(om.getOntClass("bx:Bird"));
			ng.addVertex(sn1);
			ng.addVertex(sn2);
		
		
		List<String> targets = new LinkedList<>();
		targets.add(sn2.getOntClass().getURI());
		DecisionTree decTree = new DecisionTree(parent, ng, 
				new DecisionTreeData(sn1.getOntClass().getURI(), targets));
		decTree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Question qSicakMi = decTree.addQuestion("Madde sıcak mı?");
		Question qCokMuSicak = decTree.addQuestion("Çok mu sıcak?");
			decTree.addAnswer("Evet", qSicakMi, qCokMuSicak, "Madde sıcak");
			decTree.addAnswer("Değil", qSicakMi, decTree.createBlockLeaf());
		
		Question qYaniyorMu = decTree.addQuestion("Madde yanıyor mu?", "Yanıyor mu?");
			
			decTree.addAnswer("Biraz sıcak", qCokMuSicak, decTree.createBlockLeaf());
			decTree.addAnswer("Evet", qCokMuSicak, qYaniyorMu);
		

			decTree.addAnswer("Evet", qYaniyorMu, decTree.getLeaf(sn2));
			decTree.addAnswer("Hayır", qYaniyorMu, decTree.createBlockLeaf());
			
		
		
		return decTree;
	}
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}
	
	
}