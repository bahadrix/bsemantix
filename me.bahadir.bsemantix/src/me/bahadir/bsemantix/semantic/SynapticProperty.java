package me.bahadir.bsemantix.semantic;

import com.hp.hpl.jena.rdf.model.Property;

public class SynapticProperty {

	private Property owlProperty;

	public SynapticProperty(Property owlProperty) {
		this.owlProperty = owlProperty;
	}

	public Property getOwlProperty() {
		return owlProperty;
	}
	
	
	
}
