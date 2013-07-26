package me.bahadir.bsemantix.parts.metaeditor;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ccortex.CCortex;
import me.bahadir.bsemantix.ngraph.NodeMeta;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree.DecisionTreeData;
import me.bahadir.bsemantix.ngraph.dtree.Leaf.LeafData;

import org.eclipse.swt.widgets.Composite;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Resource;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CSynapticIndividual extends MetaField {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text;
	private Composite parent;
	private LeafData leafData;


	public CSynapticIndividual(Composite parent, Individual individual,
			NodeMeta nodeMeta) {
		super(parent, individual, nodeMeta);
		this.parent = parent;
		setLayout(new GridLayout(2, false));
		
		text = new Text(this, SWT.BORDER);
		text.setEnabled(false);
		text.setEditable(false);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text, true, true);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startDecider();
			}
		});
		btnNewButton.setToolTipText("Decide!");
		btnNewButton.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/net_wrs.png"));
		formToolkit.adapt(btnNewButton, true, true);
		// TODO Auto-generated constructor stub
		
		
	}

	protected void startDecider() {
		// TODO Auto-generated method stub
		
		CCortex cortex = S.getStandartCCortex();
		DecisionTreeData treeData = cortex.load(nodeMeta);
		
		DecisionDialog dd = new DecisionDialog(parent.getShell(), treeData);
		dd.create();
		if(dd.open() == DecisionDialog.OK) {
			this.leafData = dd.getLeafData();
			text.setText(dd.getLeafData().getText());
		} 
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	boolean save() throws MetaCardSaveException {
		if(this.leafData == null) return false;
		
		individual.setPropertyValue(nodeMeta.getPredicate(), individual.getOntModel().getResource(leafData.getOutputUri()));
		
		return true;
	}

	@Override
	void load() {
		if(individual.hasProperty(nodeMeta.getPredicate())) {
			Resource r = individual.getPropertyResourceValue(nodeMeta.getPredicate());
			text.setText(r.getLocalName());
		}
		
	}
}
