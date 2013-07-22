package me.bahadir.bsemantix.ngraph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.ConversionException;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL2;

public class Restrict implements Serializable {

	public static enum PredicateType {DATA_TYPE, OBJECT_TYPE};
	

	private static final long serialVersionUID = 4550267765831103782L;

	protected static Logger log = Logger.getLogger(Restrict.class.getSimpleName());

	public static final int STAR_CARDINAL = -1;

	private OntClass range;
	private Resource dataRange;
	private OntClass source;
	private Property predicate;
	private int minCardinality = STAR_CARDINAL;
	private int maxCardinality = STAR_CARDINAL;
	private int exactCardinality = STAR_CARDINAL;
	private Restriction owlRestriction;

	private PredicateType predicateType = PredicateType.OBJECT_TYPE;
	

	public static Restrict createFromOwlRestriction(OntClass ontClass, Restriction owlRestriction) {
		return createFromOwlRestriction(ontClass, owlRestriction, false);
	}
	public static Restrict createFromOwlRestriction(OntClass ontClass, Restriction owlRestriction, boolean allowLiterals) {
		Restrict r = new Restrict(ontClass, owlRestriction);		
		if(!allowLiterals && r.predicateType == PredicateType.DATA_TYPE) {
			System.out.println("literal switch is off!");
			return null;
		} else {
			return r;
		}
	}

	private Restrict(OntClass ontClass, Restriction owlRestriction) {
		this.owlRestriction = owlRestriction;

		setSource(ontClass);
		setPredicate(owlRestriction.getOnProperty());
		resolve();
		

	}
	


	private void resolve() {
		Resource range = null;
		OntClass rangeClass = null;

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
				range = owlRestriction.getPropertyValue(OWL2.onDataRange).asResource();
			} else if (owlRestriction.hasProperty(OWL2.onClass)) {
				range = owlRestriction.getPropertyValue(OWL2.onClass).asResource();
				
			}
		
					
		}

		if (range != null) {
			if(range.canAs(OntClass.class)) {
				this.predicateType = PredicateType.OBJECT_TYPE;
				setRange(range.as(OntClass.class));
			} else {
				this.predicateType = PredicateType.DATA_TYPE;
				setDataRange(range);
			}
			
		}
	}

	public PredicateType getPredicateType() {
		return predicateType;
	}
	public Resource getDataRange() {
		return dataRange;
	}

	public void setDataRange(Resource dataRange) {
		this.dataRange = dataRange;
	}

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}

	public OntClass getRange() {
		return range;
	}

	public void setRange(OntClass range) {
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
