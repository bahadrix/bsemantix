package me.bahadir.bsemantix.parts.metaeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Property;

public class CLiteralString extends MetaField {


	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Text text;


	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CLiteralString(Composite parent, Individual ind, Property property) {
		super(parent, ind, property);
		
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(2, false));
		
		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(text, true, true);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setToolTipText("Edit Text");
		btnNewButton.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/page-pencil-16-ns.png"));
		toolkit.adapt(btnNewButton, true, true);


	}


	@Override
	boolean save() {
		if(text.getText().trim().length() > 0) {
			individual.addLiteral(property, text.getText());
			return true;
		}
		
		
		return false;
		
		
		
	}

	@Override
	void load() {	
		String value = individual.getPropertyValue(property) != null 
				? individual.getPropertyValue(property).asLiteral().getString()
				: "";
				
		this.text.setText(value);
	}



}
