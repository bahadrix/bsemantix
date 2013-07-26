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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;

public class MetaCard extends FormDialog {
	protected static Logger log = Logger.getLogger(MetaCard.class.getSimpleName());
	public List<DatatypeProperty> dataProperties;
	
	private Text text;
	private Individual individual = null;
	private OntClass ontClass = null;
	private Text text_1;
	private List<MetaField> metaFields;
	private Text txtName;

	private Composite cSingles;
	private Composite cMultiples;
	private CTabFolder tabFolder;
	private CTabItem tabNewItem;
	private CTabItem tabItem;
	private Button btnNewButton;
	
	private IManagedForm managedForm;
	
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

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(String.format("Create %s instance", ontClass.getLocalName()));
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		
		cSingles = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		cSingles.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		managedForm.getToolkit().adapt(cSingles);
		managedForm.getToolkit().paintBordersFor(cSingles);
		GridLayout gl_cSingles = new GridLayout(2, false);
		gl_cSingles.verticalSpacing = 0;
		cSingles.setLayout(gl_cSingles);
		
		
		
		
		

		addMetaPro(managedForm, NodeMeta.createFromProperty(ontClass, DC.title, XSD.xstring, true), "Title");
		
		loadOntClass(managedForm);

	}

	private void addCMultiples() {
		cMultiples = new Composite(managedForm.getForm().getBody(), SWT.NONE);
		cMultiples.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		managedForm.getToolkit().adapt(cMultiples);
		managedForm.getToolkit().paintBordersFor(cMultiples);
		cMultiples.setLayout(new GridLayout(1, false));
		
		tabFolder = new CTabFolder(cMultiples, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		managedForm.getToolkit().adapt(tabFolder);
		managedForm.getToolkit().paintBordersFor(tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		
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

		
		MetaField metaField = null;

		log.info(nodeMeta.getPredicateLabel());

		switch (nodeMeta.getPredicateType()) {
		case DATA_TYPE:
		
			addSingleLabel(nodeMeta.getPredicateLabel());
			
			metaField = new CLiteralString(cSingles, individual, nodeMeta);
			
			break;
		case OBJECT_TYPE:

			if (!nodeMeta.isToOne()) { // * to many
				if(cMultiples == null) {
					addCMultiples();
				}
				CTabItem tab = new CTabItem(tabFolder, SWT.NONE);
				tab.setText(nodeMeta.getPredicateLabel());
				metaField = new CIndividualList(tabFolder, individual, nodeMeta);
				tab.setControl(metaField);
			

			} else {
				
				addSingleLabel(nodeMeta.getPredicateLabel());
				if (nodeMeta.isSynaptic()) { // * to one
					metaField = new CSynapticIndividual(cSingles, individual, nodeMeta);

				} else {
					metaField = new CIndividualSelect(cSingles, individual, nodeMeta);

				}

			}
			break;
		}
		// if(restrict.getDataRange().equals(XSD.xstring)) {
		if (metaField != null) {
			metaField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			managedForm.getToolkit().adapt(metaField, true, true);
			metaFields.add(metaField);
		}

		managedForm.getForm().getBody().layout(true, true);

	}

	private void addSingleLabel(String text) {
		Label lblNewLabel = new Label(cSingles, SWT.NONE);
		lblNewLabel.setText(text);
		managedForm.getToolkit().adapt(lblNewLabel, true, true);
		
	}
}
