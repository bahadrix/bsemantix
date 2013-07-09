package me.bahadir.bsemantix.ngraph;

import me.bahadir.bsemantix.semantic.SynapticProperty;

import com.hp.hpl.jena.ontology.OntClass;

public class Synapse {

	public static final int STAR_CARDINAL = -1;

	private SynapticProperty property;
	private OntClass range;
	private OntClass source;
	private int minCardinality = STAR_CARDINAL;
	private int maxCardinality = STAR_CARDINAL;
	private int exactCardinality = STAR_CARDINAL;
	
	public Synapse(SynapticProperty property) {
		this.property = property;
	}
	public SynapticProperty getProperty() {
		return property;
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
