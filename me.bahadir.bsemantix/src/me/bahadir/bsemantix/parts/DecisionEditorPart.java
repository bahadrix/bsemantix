package me.bahadir.bsemantix.parts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
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

import me.bahadir.bsemantix.Activator;
import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.dtree.Answer.AnswerData;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree.DecisionTreeData;
import me.bahadir.bsemantix.ngraph.dtree.Answer;
import me.bahadir.bsemantix.ngraph.dtree.Leaf;
import me.bahadir.bsemantix.ngraph.dtree.SynonymSet;
import me.bahadir.bsemantix.ngraph.dtree.Leaf.LeafData;
import me.bahadir.bsemantix.ngraph.dtree.Leaf.LeafType;
import me.bahadir.bsemantix.ngraph.dtree.Question;
import me.bahadir.bsemantix.ngraph.dtree.Question.QuestionData;
import me.bahadir.bsemantix.ngraph.dtree.SynonymSet;
import me.bahadir.bsemantix.semantic.SampleOM;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class DecisionEditorPart {
	protected static Logger log = Logger.getLogger(DecisionEditorPart.class.getSimpleName());
	private Shell shell;
	private DecisionTree activeTree;
	private Menu addLeafMenu;

	@Inject
	public DecisionEditorPart() {
		// TODO Your code here
	}

	@PostConstruct
	public void postConstruct(final Composite parent, Shell shell) {
		this.shell = shell;
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

		final ToolItem tbImportXML = new ToolItem(bar, SWT.PUSH);
		tbImportXML.setText("Import");
		tbImportXML.setImage(S.getImage("icons/xml.png"));
		tbImportXML.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				importXML();
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
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[]{"*.xml"});
		dialog.setOverwrite(true);
		
		String path = dialog.open();
		if(path == null) return;

		try {
			File file = new File(path);
			if(file.exists() && !dialog.getOverwrite()) {
				return;
			}
			
			activeTree.saveXML(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//	

		
	}

	
	private void importXML() {
		
		FileDialog dialog = new FileDialog(shell);
		dialog.setFilterExtensions(new String[]{"*.xml"});
		String path = dialog.open();
		
		
		
		if(path == null) return;
		
		

		   //S.showErrorDialog("Hata", new Exception("yalan dolan hep"), shell);
		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(DecisionTreeData.class);
	 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					switch (event.getSeverity()) {
					case ValidationEvent.FATAL_ERROR:
						log.severe("Can not read decision xml file. Please check format..");
						return true;
					}
					return false;
				}
			});
			DecisionTreeData dData = (DecisionTreeData) jaxbUnmarshaller.unmarshal(file);
			reset();
			activeTree.loadData(dData);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	
	}

	private GraphNode recurseParse(Element mother) {
		GraphNode result = null;
		if (mother.getTagName().equals("question")) {

			Question q = new Question(activeTree, QuestionData.createFromElement(mother));
			result = q;
			NodeList answerNodes = mother.getChildNodes();

			for (int i = 0; i < answerNodes.getLength(); i++) {
				if (answerNodes.item(i) instanceof Element) {
					Element eAnswer = (Element) answerNodes.item(i);
					GraphNode target = null;

					Element firstElement = S.getFirstElementOfTag(eAnswer, "question", "leaf");


					if (firstElement == null) {
						log.severe(q.getText() + " answer must have exactly 1 endpoint");
					} else {
						if(firstElement.getNodeName().equals("question")) {
							target = recurseParse(firstElement);
						} else {
							target =new Leaf(activeTree, LeafData.createFromElement(firstElement));
						}
						Answer a = new Answer(activeTree, q, target, 
								new AnswerData(eAnswer.getAttribute("text"), eAnswer.getAttribute("fact"), null));
					}
					

			
					

				}
			}

		} // eof if question element
		
		return result;

	}

	private void reset() {

		activeTree.disposeChildren();

		// Dispose leaf menu
		while (addLeafMenu.getItemCount() > 0) {
			addLeafMenu.getItem(0).dispose();
		}

	}

	protected void addQuestion() {
		// TODO Auto-generated method stub
		int selectSize = activeTree.getSelection().size();
		if (selectSize < 1) {
			log.warning("You must select a question node");
			return;
		}

		Object object = activeTree.getSelection().get(selectSize - 1);
		if (object instanceof Question) {
			Question source = (Question) object;
			Question target = activeTree.addQuestion("<question>");
			activeTree.addAnswer(source, target, new AnswerData("<answer>", "<fact>"));
		} else {
			log.warning("You must select a question node");

		}
	}

	private void addLeaf(Leaf leaf) {

		int selectSize = activeTree.getSelection().size();
		if (selectSize < 1) {
			log.warning("You must select a question node");
			return;
		}

		Object object = activeTree.getSelection().get(selectSize - 1);
		if (object instanceof Question) {
			Question source = (Question) object;
			Leaf targetLeaf = leaf == null ? activeTree.createBlockLeaf() : leaf;

			if (!activeTree.hasDirectedConnection(source, targetLeaf)) {

				activeTree.addAnswer(source, targetLeaf, new AnswerData("<answer>", "<fact>"));
				onItemAdded();
			} else {
				log.warning("these nodes already connected");
			}

		} else {
			log.warning("You must select a question node");

		}
		// if(leaf == null) leaf = activeTree.createBlockLeaf();

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

		if (activeTree.getOutputs().size() > 0) {
			MenuItem seperator = new MenuItem(addLeafMenu, SWT.SEPARATOR);

		}
		for (final Leaf leaf : activeTree.getOutputs().values()) {
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
		DecisionTree decTree = new DecisionTree(parent, ng, new DecisionTreeData(sn1.getOntClass().getURI(), targets));
		decTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		Question qSicakMi = decTree.addQuestion("Madde sıcak mı?");
		Question qCokMuSicak = decTree.addQuestion("Çok mu sıcak?");
		decTree.addAnswer(qSicakMi, qCokMuSicak, new AnswerData("evet", "madde sıcak"));
		decTree.addAnswer(qSicakMi, decTree.createBlockLeaf(), new AnswerData("hayır", "madde sıcak değil"));

		Question qYaniyorMu = decTree.addQuestion("Madde yanıyor mu?", "Yanıyor mu?");

		decTree.addAnswer(qCokMuSicak, decTree.createBlockLeaf(), new AnswerData("biraz sıcak", "madde biraz sıcak"));
		decTree.addAnswer(qCokMuSicak, qYaniyorMu, new AnswerData("evet","madde çok sıcak"));

		decTree.addAnswer(qYaniyorMu, decTree.getLeaf(sn2), new AnswerData("evet","madde yanıyor"));
		decTree.addAnswer(qYaniyorMu, decTree.createBlockLeaf(), new AnswerData("hayır", "madde sönük"));

		return decTree;

	}

	@Focus
	public void onFocus() {
		// TODO Your code here
	}

}