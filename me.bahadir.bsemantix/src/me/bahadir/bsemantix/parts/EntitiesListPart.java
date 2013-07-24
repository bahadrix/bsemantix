 
package me.bahadir.bsemantix.parts;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.BenchObject;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.SynapticEdge;
import me.bahadir.bsemantix.parts.metaeditor.MetaCard;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jgrapht.GraphPath;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class EntitiesListPart extends ViewPart{
	protected static Logger log = Logger.getLogger(EntitiesListPart.class.getSimpleName());
	public static final String TOPIC_SET_GRAPH = "PATHS_PART/SET_PATH";
	public static final String TOPIC_SET_SELECTION = "PATHS_PART/SET_SELECTION";
	public static final String TOPIC_OPEN_SYNAPTIC_EDGE = "PATHS_PART/OPEN_SYNAPTIC_EDGE";
	
	
	private TreeViewer treeViewer;

	@Inject
	public EntitiesListPart() {
		//TODO Your code here
	}
	
	public static class EntitySet {
		public final List<BenchObject> selection;
		public final String title;
		public EntitySet(List<BenchObject> selection, String title) {
			this.selection = selection;
			this.title = title;
		}
		
		public EntitySet(GraphPath<SphereNode, NeuralEdge> path, String title) {
			this(S.getCrumbles(path), title);
		}
	}
	
	@PostConstruct
	public void postConstruct(final Composite parent, final EPartService partService) {
		
		

		
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.setContentProvider(new SelectionContentProvider());
		treeViewer.setLabelProvider(new PathLabelProvider());
		treeViewer.setAutoExpandLevel(2);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel =  (IStructuredSelection) event.getSelection();
				Object item = sel.getFirstElement();
				if(item instanceof TreePair) {
					TreePair pair = (TreePair) item;
					pair.onDoubleClicked();
				} else if(item instanceof SynapticEdge) {
					partService.activate(partService.findPart("me.bahadir.bsemantix.part.DecisionEditor"));
					S.broker.post(DecisionEditorPart.TOPIC_EDIT_SYNAPTIC_EDGE, item);
				} else if(item instanceof SphereNode) {
					SphereNode sn = (SphereNode) item;
					OntClass cls = sn.getOntClass();
					ExtendedIterator<? extends OntResource> inds = cls.listInstances();
					
					Individual ind = null;
					
					while(inds.hasNext()) {
						ind = inds.next().asIndividual();
					}
					
					MetaCard mc = new MetaCard(parent.getShell(), sn.getOntClass(), ind);
					mc.create();
					mc.getShell().setSize(500, 600);
					mc.open();
					sn.getOntClass().getOntModel().write(System.out);
				}
			}
		});
	}
	

	
	@Inject @Optional
	public void setPath(@UIEventTopic(TOPIC_SET_GRAPH) GraphPath<SphereNode, NeuralEdge> graphpath) {
	
		
	}
	
	@Inject @Optional
	public void setSelection(@UIEventTopic(TOPIC_SET_SELECTION) EntitySet[] entitySets) {

		treeViewer.setInput(entitySets);
		
	}
	@Inject @Optional
	public void setSelection(@UIEventTopic(TOPIC_SET_SELECTION) EntitySet entitySet) {

		treeViewer.setInput(new EntitySet[] {entitySet});
		
	}
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	
}