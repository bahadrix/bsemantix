package me.bahadir.bsemantix.parts.metaeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public abstract class MetaField extends Composite
{
	protected Property property;
	protected Individual individual;
	
	abstract boolean save();
	abstract void load();
	
	
	public MetaField(Composite parent, Individual individual, Property property) {
		super(parent, SWT.None);
		this.property = property;
		this.individual = individual;
	}
	
	
	

}
