package me.bahadir.bsemantix.parts.metaeditor;

import me.bahadir.bsemantix.ngraph.NodeMeta;

import org.eclipse.swt.widgets.Composite;

import com.hp.hpl.jena.ontology.Individual;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.ResourceManager;

public class CSynapticIndividual extends MetaField {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text;



	public CSynapticIndividual(Composite parent, Individual individual,
			NodeMeta nodeMeta) {
		super(parent, individual, nodeMeta);
		setLayout(new GridLayout(2, false));
		
		text = new Text(this, SWT.BORDER);
		text.setEditable(false);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text, true, true);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setToolTipText("Decide!");
		btnNewButton.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/net_wrs.png"));
		formToolkit.adapt(btnNewButton, true, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	boolean save() throws MetaCardSaveException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void load() {
		// TODO Auto-generated method stub
		
	}
}
