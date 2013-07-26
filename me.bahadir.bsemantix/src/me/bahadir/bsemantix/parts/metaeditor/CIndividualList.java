package me.bahadir.bsemantix.parts.metaeditor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.bahadir.bsemantix.ngraph.NodeMeta;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

public class CIndividualList extends MetaField {
	
	protected static Logger log = Logger.getLogger(CIndividualList.class.getSimpleName());
	private static Image greenBullet = ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/circle-green-16-ns.png");

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private List<IndiWrapper> items;
	private Table table;
	
	public class IndiWrapper {
		private String label;
		private Resource resource;
		public IndiWrapper(Resource resource) {
			this.resource = resource;
			this.label = resource.getLocalName();
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public Resource getResource() {
			return resource;
		}
		public void setResource(Resource resource) {
			this.resource = resource;
		}
		
		
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CIndividualList(Composite parent, Individual individual, NodeMeta nodeMeta) {
		super(parent, individual, nodeMeta);
		this.items = new ArrayList<>();
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(toolBar);
		toolkit.paintBordersFor(toolBar);
		
		ToolItem tbAdd = new ToolItem(toolBar, SWT.NONE);
		tbAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addIndividual();
			}
		});
		tbAdd.setToolTipText("Add Item");
		tbAdd.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/badge-square-plus-24-ns.png"));
		
		ToolItem tbEdit = new ToolItem(toolBar, SWT.NONE);
		tbEdit.setToolTipText("Edit Item");
		tbEdit.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/page-pencil-24-ns.png"));
		
		ToolItem tbRemove = new ToolItem(toolBar, SWT.NONE);
		tbRemove.setToolTipText("Remove Item");
		tbRemove.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/badge-circle-cross-24-ns.png"));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		toolkit.adapt(composite);
		toolkit.paintBordersFor(composite);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		toolkit.adapt(table);
		toolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn clName = new TableColumn(table, SWT.NONE);
		clName.setWidth(166);
		clName.setText("Name");
		
		TableColumn clURI = new TableColumn(table, SWT.NONE);
		clURI.setWidth(237);
		clURI.setText("URI");

		


	}

	

	protected void addIndividual() {
		OntClass targetClass = nodeMeta.getRange().as(OntClass.class);
		
		MetaCard mc = new MetaCard(getShell(), targetClass, null);
		mc.create();
		
		if(mc.open() == MetaCard.OK) {
		
			individual.addProperty(nodeMeta.getPredicate(), mc.getIndividual());
			load();
		}
		
		
	}



	@Override
	boolean save() throws MetaCardSaveException {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void reset() {
		table.clearAll();
	}

	@Override
	void load() {
		reset();
		log.info("loading 1*");
		StmtIterator values = individual.listProperties(nodeMeta.getPredicate());
		
		while(values.hasNext()) {
			
			Statement st = values.next();
			
			log.info("Loading list item: " + st.getResource().getURI());
			TableItem ti = new TableItem(table, SWT.None);
			ti.setImage(greenBullet);
			ti.setText(new String[]{
					st.getResource().getLocalName(),
					st.getResource().getURI()
			});
			ti.setData(st.getResource());
		}

		
	}

}
