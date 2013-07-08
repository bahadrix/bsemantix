 
package me.bahadir.bsemantix.parts;

import java.io.StringReader;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.semantic.OntoAdapter;

import org.apache.jena.atlas.logging.Log;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.InvalidPropertyURIException;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;

public class EditorPart {
	protected static Logger log = Logger.getLogger(EditorPart.class.getSimpleName());
	private StyledText txtEditor;
	
	@Inject
	public EditorPart() {
		
	}
	
	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout());
		ToolBar bar = new ToolBar(parent, SWT.FLAT);
		
		final ToolItem tbShortPath = new ToolItem(bar, SWT.PUSH);
		tbShortPath.setText("Parse");
		
		tbShortPath.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				parseOwl();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		bar.pack();
		
		Font font = new Font(parent.getDisplay(), "Courier New", 10, SWT.NORMAL);
		
		txtEditor = new StyledText(parent, SWT.FLAT | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		txtEditor.setFont(font);
		//txtEditor.setEditable(false);
		txtEditor.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		
	}
	
	public void parseOwl() {
		//Model model = ModelFactory.createDefaultModel();
		OntModel model = ModelFactory.createOntologyModel(); 
		StringReader sr = new StringReader(txtEditor.getText());
		
		model.read(sr, "http://bahadir.me/organiclegislation/");
		
		OntoAdapter adapter = new OntoAdapter(model);
		adapter.showSynapticStatements();
		
	}
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}
	
	
}