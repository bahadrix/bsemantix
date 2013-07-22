package me.bahadir.bsemantix.parts.metaeditor;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import me.bahadir.bsemantix.ngraph.Restrict;
import me.bahadir.bsemantix.ngraph.Restrict.PredicateType;

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
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.XSD;

public class MetaCard extends FormDialog {
	protected static Logger log = Logger.getLogger(MetaCard.class.getSimpleName());
	public List<DatatypeProperty> dataProperties;
	
	private Text text;
	private Individual individual = null;
	private OntClass ontClass = null;
	private Text text_1;
	private List<MetaField> metaFields;
	
	/**
	 * @wbp.parser.constructor
	 */
	public MetaCard(Shell shell, OntClass cls) {
		super(shell);
		this.ontClass = cls;
		setHelpAvailable(false);
		this.metaFields = new LinkedList<>();
	}

	private void loadOntClass(IManagedForm managedForm) {
	
		dataProperties = new LinkedList<>();
		
		// Transaction dialog'un dýþýnda yapýlmalý, cancel ederse rrevert edilmeli.
		//		log.info("Starting transaction..");
		//		ontClass.getOntModel().begin();
		
		
		
		this.individual = ontClass.createIndividual();
		
		
		ExtendedIterator<OntClass> exeq = ontClass.listEquivalentClasses();
		
		while(exeq.hasNext()) {
			OntClass eqClass = exeq.next();
			if(eqClass.isRestriction()) {
				
				Restriction rest = eqClass.asRestriction();
				Restrict restrict = Restrict.createFromOwlRestriction(ontClass, rest, true);
				if(restrict.getPredicateType() == PredicateType.DATA_TYPE) {					
					
					addMetaPro(managedForm,	restrict);
					
				}

			}
		}
		
		for(MetaField mf : metaFields) {
			mf.load();
		}
	}
	
	
	private boolean saveAll() {
		boolean totalSuccess = true;
		for(MetaField mf : metaFields) {
			boolean succ = mf.save();
			if(!succ) System.out.println(mf);
			totalSuccess &= succ;	
		}
		return totalSuccess;
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

	private <T> void addMetaPro(IManagedForm managedForm, Restrict restrict) {
		
		Label lblNewLabel = new Label(managedForm.getForm().getBody(), SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		managedForm.getToolkit().adapt(lblNewLabel, true, true);
		lblNewLabel.setText(restrict.getPredicate().getLocalName());

		//if(restrict.getDataRange().equals(XSD.xstring)) {
		MetaField metaField = new CLiteralString(managedForm.getForm().getBody(), individual, restrict.getPredicate());
		metaField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		managedForm.getToolkit().adapt(metaField, true, true);
		
		metaFields.add(metaField);

	}
	
	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Create Person Instance");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(2, false));

		
		loadOntClass(managedForm);

	}

}
