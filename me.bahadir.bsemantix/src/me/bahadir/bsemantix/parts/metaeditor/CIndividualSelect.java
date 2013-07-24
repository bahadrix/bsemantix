package me.bahadir.bsemantix.parts.metaeditor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NodeMeta;
import me.bahadir.bsemantix.parts.metaeditor.MetaField.MandatoryFieldNotFilled;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CIndividualSelect extends MetaField {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Combo combo;

	private List<Resource> indlist;
	
	public CIndividualSelect(Composite parent, Individual individual, NodeMeta nodeMeta) {
		super(parent, individual, nodeMeta);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(2, false));
		
		combo = new Combo(this, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(combo);
		toolkit.paintBordersFor(combo);
		if(nodeMeta.isExtensible()) {
			Button btnNewButton = toolkit.createButton(this, "", SWT.NONE);
			btnNewButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					addNewIndvidual();
				}
			});
			btnNewButton.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/arrow-incident-red-16-ns.png"));
		}
	}


	
	private void addNewIndvidual() {

		
		MetaCard mc = new MetaCard(getShell(), nodeMeta.getRange().as(OntClass.class), null);
		mc.create();
		mc.getShell().setSize(500, 600);
		mc.open();
		load();
		for(int i = 0; i < combo.getItemCount(); i++) {
			if(combo.getItem(i).equals(S.getIndividualCaption(mc.getIndividual()))) {
				combo.select(i);
				break;
			}
		}
		
	}
	
	@Override
	boolean save() throws MetaCardSaveException {
		int sin = combo.getSelectionIndex();
		if(sin == -1) {
			if(nodeMeta.getExactCardinality() > 0 || nodeMeta.getMinCardinality() > 0) {
				throw new MandatoryFieldNotFilled(this, label + " must filled");
			}
			
		} else {
			individual.addProperty(nodeMeta.getPredicate(), indlist.get(sin));
			
		}
		return true;
	}

	@Override
	void load() {
		indlist = new ArrayList<>();
		combo.removeAll();
		
		ExtendedIterator<? extends OntResource> exit = nodeMeta.getRange().as(OntClass.class).listInstances();
		
		while(exit.hasNext()) {
			Individual ind = (Individual) exit.next();
			if(ind.getURI() != null) {
				indlist.add(ind);
				combo.add(S.getIndividualCaption(ind));
			}
		}
		
	}
}
