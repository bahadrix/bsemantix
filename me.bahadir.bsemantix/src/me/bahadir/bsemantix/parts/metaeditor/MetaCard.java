package me.bahadir.bsemantix.parts.metaeditor;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NodeMeta;
import me.bahadir.bsemantix.ngraph.NodeMeta.PredicateType;
import me.bahadir.bsemantix.parts.metaeditor.MetaField.MetaCardSaveException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.wb.swt.ResourceManager;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.XSD;

public class MetaCard extends FormDialog {
	protected static Logger log = Logger.getLogger(MetaCard.class.getSimpleName());
	public List<DatatypeProperty> dataProperties;
	
	private Text text;
	private Individual individual = null;
	private OntClass ontClass = null;
	private Text text_1;
	private List<MetaField> metaFields;
	private Text txtName;
	
	/**
	 * @wbp.parser.constructor
	 */
	public MetaCard(Shell shell, OntClass cls, Individual individual) {
		super(shell);
		
		if(cls == null) log.severe("OntClass cant be null");
		this.ontClass = cls;
		this.individual = individual == null ? cls.createIndividual(String.format("%sind%d", cls.getNameSpace(), System.currentTimeMillis())) : individual;
		this.metaFields = new LinkedList<>();
		setHelpAvailable(false);
	}

	private void loadOntClass(IManagedForm managedForm) {
	
		dataProperties = new LinkedList<>();
		
		// Transaction dialog'un dýþýnda yapýlmalý, cancel ederse rrevert edilmeli.
		//		log.info("Starting transaction..");
		//		ontClass.getOntModel().begin();
		
		
		
		ExtendedIterator<OntClass> exeq = ontClass.listEquivalentClasses();
		
		while(exeq.hasNext()) {
			OntClass eqClass = exeq.next();
			if(eqClass.isRestriction()) {
				
				Restriction rest = eqClass.asRestriction();
				NodeMeta nodeMeta = NodeMeta.createFromOwlRestriction(ontClass, rest, true);
				addMetaPro(managedForm,	nodeMeta);

			}
		}
		
		for(MetaField mf : metaFields) {
			mf.load();
		}
	}
	
	
	
	public Individual getIndividual() {
		return individual;
	}

	private boolean saveAll() {
		boolean totalSuccess = true;
		try {
			saveBundleProps();
			for (MetaField mf : metaFields) {
				boolean succ = mf.save();
				if (!succ)
					System.out.println(mf);
				totalSuccess &= succ;
			}
			
		} catch (MetaCardSaveException me) {
			log.info(me.getMessage());
		}
		return totalSuccess;
	}
	
	private void saveBundleProps() throws MetaCardSaveException {
//		if(txtName.getText().trim().length() < 1)
//			throw new MetaCardSaveException(null, "Must enter name");
//		
//		individual.setPropertyValue(DC.title, value);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == OK) {
			if(!saveAll()) {
				return;
			}
			
		}
		super.buttonPressed(buttonId);
	}

	private void addMetaPro(IManagedForm managedForm, NodeMeta nodeMeta) {
		addMetaPro(managedForm, nodeMeta, null);
		
	}
	private void addMetaPro(IManagedForm managedForm, NodeMeta nodeMeta, String label) {
		
		Label lblNewLabel = new Label(managedForm.getForm().getBody(), SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		managedForm.getToolkit().adapt(lblNewLabel, true, true);
		if(label == null) {
			lblNewLabel.setText(S.getPropertyCaption(nodeMeta.getSource().getOntModel(), nodeMeta.getPredicate()));
		} else {
			lblNewLabel.setText(label);
		}

		MetaField metaField = null;
		
		log.info(nodeMeta.getPredicateLabel());
		
		switch(nodeMeta.getPredicateType()) {
		case DATA_TYPE:
			
			metaField = new CLiteralString(managedForm.getForm().getBody(), individual, nodeMeta);
			
			break;
		case OBJECT_TYPE:
			metaField = new CIndividualSelect(managedForm.getForm().getBody(), individual, nodeMeta);
			lblNewLabel.setText(nodeMeta.getPredicateLabel() +" : " + nodeMeta.getRangeLabel());
			break;
		}
		//if(restrict.getDataRange().equals(XSD.xstring)) {
		if(metaField != null) {
			metaField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			managedForm.getToolkit().adapt(metaField, true, true);
			metaFields.add(metaField);
		}

	}
	
	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(String.format("Create %s instance", ontClass.getLocalName()));
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(2, false));
		
		

		addMetaPro(managedForm, NodeMeta.createFromProperty(ontClass, DC.title, XSD.xstring, true), "Title");
		
		loadOntClass(managedForm);

	}





}
