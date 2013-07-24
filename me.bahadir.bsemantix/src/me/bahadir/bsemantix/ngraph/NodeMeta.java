package me.bahadir.bsemantix.ngraph;

import java.io.Serializable;
import java.util.logging.Logger;

import me.bahadir.bsemantix.S;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;

public class NodeMeta implements Serializable {

	public static enum PredicateType {DATA_TYPE, OBJECT_TYPE};
	

	private static final long serialVersionUID = 4550267765831103782L;

	protected static Logger log = Logger.getLogger(NodeMeta.class.getSimpleName());

	public static final int STAR_CARDINAL = -1;

	private Resource range;
	private OntClass source;
	private Property predicate;
	private int minCardinality = STAR_CARDINAL;
	private int maxCardinality = STAR_CARDINAL;
	private int exactCardinality = STAR_CARDINAL;


	private PredicateType predicateType = PredicateType.OBJECT_TYPE;
	

	public static NodeMeta createFromProperty(OntClass ontClass, Property property, Resource type, boolean mandatory) {
		
		NodeMeta nodeMeta = new NodeMeta(ontClass);
		nodeMeta.predicateType = PredicateType.DATA_TYPE;
		if(mandatory) nodeMeta.setExactCardinality(1);
		nodeMeta.setPredicate(property);
		nodeMeta.setRange(type);
		return nodeMeta;
		
	}
	
	public static NodeMeta createFromOwlRestriction(OntClass ontClass, Restriction owlRestriction, boolean allowLiterals) {
		NodeMeta nodeMeta = new NodeMeta(ontClass);		
		if(!allowLiterals && nodeMeta.predicateType == PredicateType.DATA_TYPE) {
			System.out.println("literal switch is off!");
			return null;
		} else {
			nodeMeta.setPredicate(owlRestriction.getOnProperty());
			nodeMeta.resolve(owlRestriction);
			return nodeMeta;
		}
	}

	/**
	 * Constructor
	 * @param ontClass
	 */
	private NodeMeta(OntClass ontClass) {
		setSource(ontClass);

	}
	

	
	public String getRangeLabel() {
		OntResource ores = source.getOntModel().getOntResource(range);
		if(ores.getLabel("tr") != null) {
			return ores.getLabel("tr");
		} else {
			return ores.getLocalName();
		}
	}
	
	public String getPredicateLabel() {
		return S.getPropertyCaption(source.getOntModel(), predicate);
		
	}
	
	public OntProperty getOntPredicate() {
		return source.getOntModel().getOntProperty(predicate.getURI());
	}

	private void resolve(Restriction owlRestriction) {
	


		// CARDINALITY 0-*
		if (owlRestriction.isSomeValuesFromRestriction()) { // some values
															// restriction
			setMinCardinality(0);
			SomeValuesFromRestriction someRest = owlRestriction.asSomeValuesFromRestriction();
			
			range = someRest.getSomeValuesFrom();

		} else if (owlRestriction.isAllValuesFromRestriction()) { // CARDINALITY
																	// 0-*
			setMinCardinality(0);
			AllValuesFromRestriction allRest = owlRestriction.asAllValuesFromRestriction();
			range = allRest.getAllValuesFrom();
		} else {

			if (owlRestriction.hasProperty(OWL2.qualifiedCardinality)) { // CARDINALITY
																// n
				int cardInt = owlRestriction.getPropertyValue(OWL2.qualifiedCardinality).asLiteral().getInt();
				setExactCardinality(cardInt);
			} else if (owlRestriction.hasProperty(OWL2.minQualifiedCardinality)) { // CARDINALITY
				int cardInt = owlRestriction.getPropertyValue(OWL2.minQualifiedCardinality).asLiteral().getInt();
				setMinCardinality(cardInt);
			} else if (owlRestriction.hasProperty(OWL2.maxQualifiedCardinality)) { // CARDINALITY																// *-n
				int cardInt = owlRestriction.getPropertyValue(OWL2.maxQualifiedCardinality).asLiteral().getInt();
				setMaxCardinality(cardInt);
			} else {
				// log.warning("Restriction format is not supported");
			}
			
			if(owlRestriction.hasProperty(OWL2.onDataRange) ) {
				this.predicateType = PredicateType.DATA_TYPE;
				range = owlRestriction.getPropertyValue(OWL2.onDataRange).asResource();
			} else if (owlRestriction.hasProperty(OWL2.onClass)) {
				this.predicateType = PredicateType.OBJECT_TYPE;
				range = owlRestriction.getPropertyValue(OWL2.onClass).asResource();
				
			}
		
					
		}

	}

	public boolean isExtensible() {
		return // extensible olabilmesi icin
				getRange() != null // hedef bos olmayacak 
				&& getRange().canAs(OntClass.class); // hedef bir owl2 sinif olacak
	}
	
	public PredicateType getPredicateType() {
		return predicateType;
	}

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}

	public Resource getRange() {
		return range;
	}

	public void setRange(Resource range) {
		this.range = range;
	}

	public OntClass getSource() {
		return source;
	}

	public void setSource(OntClass source) {
		this.source = source;
	}

	public int getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(int minCardinality) {
		this.minCardinality = minCardinality;
	}

	public int getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(int maxCardinality) {
		this.maxCardinality = maxCardinality;
	}

	public int getExactCardinality() {
		return exactCardinality;
	}

	public void setExactCardinality(int exactCardinality) {
		this.exactCardinality = exactCardinality;
	}

	public boolean isComplete() {
		return range != null && source != null;
	}

}
