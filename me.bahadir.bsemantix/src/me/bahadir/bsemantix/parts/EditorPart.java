 
package me.bahadir.bsemantix.parts;

import java.io.StringReader;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.semantic.OntoAdapter;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class EditorPart {
	protected static Logger log = Logger.getLogger(EditorPart.class.getSimpleName());
	private StyledText txtEditor;
	
	@Inject
	  private MInputPart thisPart;
	@Inject
	public EditorPart() {
		
	}
	
	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout());
		ToolBar bar = new ToolBar(parent, SWT.FLAT);
		
		System.out.println(thisPart);
		
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
		txtEditor.setText("<?xml version=\"1.0\"?>\r\n\r\n\r\n<!DOCTYPE rdf:RDF [\r\n    <!ENTITY synapse \"http://bahadir.me/synapse#\" >\r\n    <!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\r\n    <!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\r\n    <!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\r\n    <!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\r\n]>\r\n\r\n\r\n<rdf:RDF xmlns=\"http://bahadir.me/organiclegislation/\"\r\n     xml:base=\"http://bahadir.me/organiclegislation/\"\r\n     xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\r\n     xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\r\n     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\r\n     xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\r\n     xmlns:synapse=\"http://bahadir.me/synapse#\">\r\n    <owl:Ontology rdf:about=\"http://bahadir.me/organiclegislation/\">\r\n        <owl:versionIRI rdf:resource=\"http://bahadir.me/organiclegislation/1.0.0\"/>\r\n    </owl:Ontology>\r\n    \r\n\r\n\r\n    <!-- \r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n    //\r\n    // Annotation properties\r\n    //\r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n     -->\r\n\r\n    <owl:AnnotationProperty rdf:about=\"&synapse;synapse\"/>\r\n    <owl:AnnotationProperty rdf:about=\"http://bahadir.me/organiclegislation/question\"/>\r\n    <owl:AnnotationProperty rdf:about=\"&synapse;cardinality\">\r\n        <rdfs:subPropertyOf rdf:resource=\"&synapse;synapse\"/>\r\n        <rdfs:range rdf:resource=\"&xsd;int\"/>\r\n    </owl:AnnotationProperty>\r\n    <owl:AnnotationProperty rdf:about=\"&synapse;isSynaptic\">\r\n        <rdfs:subPropertyOf rdf:resource=\"&synapse;synapse\"/>\r\n        <rdfs:range rdf:resource=\"&xsd;boolean\"/>\r\n    </owl:AnnotationProperty>\r\n    \r\n\r\n\r\n    <!-- \r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n    //\r\n    // Datatypes\r\n    //\r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n     -->\r\n\r\n    \r\n\r\n\r\n    <!-- \r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n    //\r\n    // Object Properties\r\n    //\r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n     -->\r\n\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/organiclegislation/has -->\r\n\r\n    <owl:ObjectProperty rdf:about=\"http://bahadir.me/organiclegislation/has\">\r\n        <rdfs:subPropertyOf rdf:resource=\"&owl;topObjectProperty\"/>\r\n    </owl:ObjectProperty>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/synapse#hasStorageType -->\r\n\r\n    <owl:ObjectProperty rdf:about=\"&synapse;hasStorageType\">\r\n        <rdf:type rdf:resource=\"&owl;FunctionalProperty\"/>\r\n        <synapse:isSynaptic>1</synapse:isSynaptic>\r\n        <rdfs:domain rdf:resource=\"http://bahadir.me/organiclegislation/StorageFacility\"/>\r\n        <rdfs:range rdf:resource=\"http://bahadir.me/organiclegislation/StorageFacilityType\"/>\r\n    </owl:ObjectProperty>\r\n    \r\n\r\n\r\n    <!-- \r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n    //\r\n    // Data properties\r\n    //\r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n     -->\r\n\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/organiclegislation/emailAddress -->\r\n\r\n    <owl:DatatypeProperty rdf:about=\"http://bahadir.me/organiclegislation/emailAddress\">\r\n        <rdf:type rdf:resource=\"&owl;FunctionalProperty\"/>\r\n        <rdfs:label xml:lang=\"tr\">E-Posta Adresi</rdfs:label>\r\n        <rdfs:domain rdf:resource=\"http://bahadir.me/organiclegislation/Person\"/>\r\n    </owl:DatatypeProperty>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/organiclegislation/tehlikeliAtikDepolanabilir -->\r\n\r\n    <owl:DatatypeProperty rdf:about=\"http://bahadir.me/organiclegislation/tehlikeliAtikDepolanabilir\"/>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/synapse#address -->\r\n\r\n    <owl:DatatypeProperty rdf:about=\"&synapse;address\">\r\n        <rdfs:range rdf:resource=\"&xsd;string\"/>\r\n    </owl:DatatypeProperty>\r\n    \r\n\r\n\r\n    <!-- \r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n    //\r\n    // Classes\r\n    //\r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n     -->\r\n\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/organiclegislation/Estate -->\r\n\r\n    <owl:Class rdf:about=\"http://bahadir.me/organiclegislation/Estate\">\r\n        <owl:equivalentClass>\r\n            <owl:Restriction>\r\n                <owl:onProperty rdf:resource=\"&synapse;address\"/>\r\n                <owl:allValuesFrom rdf:resource=\"&xsd;string\"/>\r\n            </owl:Restriction>\r\n        </owl:equivalentClass>\r\n    </owl:Class>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/organiclegislation/Person -->\r\n\r\n    <owl:Class rdf:about=\"http://bahadir.me/organiclegislation/Person\">\r\n        <owl:equivalentClass>\r\n            <owl:Restriction>\r\n                <owl:onProperty rdf:resource=\"http://bahadir.me/organiclegislation/emailAddress\"/>\r\n                <owl:minQualifiedCardinality rdf:datatype=\"&xsd;nonNegativeInteger\">1</owl:minQualifiedCardinality>\r\n                <owl:onDataRange rdf:resource=\"&xsd;string\"/>\r\n            </owl:Restriction>\r\n        </owl:equivalentClass>\r\n        <owl:equivalentClass>\r\n            <owl:Restriction>\r\n                <owl:onProperty rdf:resource=\"http://bahadir.me/organiclegislation/has\"/>\r\n                <owl:someValuesFrom rdf:resource=\"http://bahadir.me/organiclegislation/StorageFacility\"/>\r\n            </owl:Restriction>\r\n        </owl:equivalentClass>\r\n        <rdfs:comment xml:lang=\"en\">Used as unique identification</rdfs:comment>\r\n    </owl:Class>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/organiclegislation/StorageFacility -->\r\n\r\n    <owl:Class rdf:about=\"http://bahadir.me/organiclegislation/StorageFacility\">\r\n        <rdfs:label rdf:datatype=\"&xsd;string\">Depolama tesisi</rdfs:label>\r\n        <owl:equivalentClass>\r\n            <owl:Restriction>\r\n                <owl:onProperty rdf:resource=\"&synapse;hasStorageType\"/>\r\n                <owl:onClass rdf:resource=\"http://bahadir.me/organiclegislation/StorageFacilityType\"/>\r\n                <owl:qualifiedCardinality rdf:datatype=\"&xsd;nonNegativeInteger\">1</owl:qualifiedCardinality>\r\n            </owl:Restriction>\r\n        </owl:equivalentClass>\r\n        <rdfs:subClassOf rdf:resource=\"http://bahadir.me/organiclegislation/Estate\"/>\r\n    </owl:Class>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/organiclegislation/StorageFacilityType -->\r\n\r\n    <owl:Class rdf:about=\"http://bahadir.me/organiclegislation/StorageFacilityType\"/>\r\n    \r\n\r\n\r\n    <!-- \r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n    //\r\n    // Individuals\r\n    //\r\n    ///////////////////////////////////////////////////////////////////////////////////////\r\n     -->\r\n\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/synapse#StroteFacilityClass1 -->\r\n\r\n    <owl:NamedIndividual rdf:about=\"&synapse;StroteFacilityClass1\">\r\n        <rdf:type rdf:resource=\"http://bahadir.me/organiclegislation/StorageFacilityType\"/>\r\n        <rdfs:label rdf:datatype=\"&xsd;string\">1. S\u0131n\u0131f Depolama Tesisi</rdfs:label>\r\n    </owl:NamedIndividual>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/synapse#StroteFacilityClass2 -->\r\n\r\n    <owl:NamedIndividual rdf:about=\"&synapse;StroteFacilityClass2\">\r\n        <rdf:type rdf:resource=\"http://bahadir.me/organiclegislation/StorageFacilityType\"/>\r\n        <rdfs:label rdf:datatype=\"&xsd;string\">2. S\u0131n\u0131f Depolama Tesisi</rdfs:label>\r\n    </owl:NamedIndividual>\r\n    \r\n\r\n\r\n    <!-- http://bahadir.me/synapse#StroteFacilityClass3 -->\r\n\r\n    <owl:NamedIndividual rdf:about=\"&synapse;StroteFacilityClass3\">\r\n        <rdf:type rdf:resource=\"http://bahadir.me/organiclegislation/StorageFacilityType\"/>\r\n        <rdfs:label rdf:datatype=\"&xsd;string\">3. S\u0131n\u0131f Depolama Tesisi</rdfs:label>\r\n    </owl:NamedIndividual>\r\n</rdf:RDF>\r\n\r\n\r\n\r\n<!-- Generated by the OWL API (version 3.3.1957) http://owlapi.sourceforge.net -->");
		txtEditor.setFont(font);
		//txtEditor.setEditable(false);
		txtEditor.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		thisPart.getContext().set("txtOntology", txtEditor);
		
		
		
	}
	
	public void parseOwl() {
		//Model model = ModelFactory.createDefaultModel();
		OntModel model = ModelFactory.createOntologyModel(); 
		StringReader sr = new StringReader(txtEditor.getText());
		
		model.read(sr, "http://bahadir.me/organiclegislation/");
		
		OntoAdapter adapter = new OntoAdapter(model);
		adapter.createNeuralGraph();
		//adapter.resolveSynapses();
		
	}
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}
	
	
}