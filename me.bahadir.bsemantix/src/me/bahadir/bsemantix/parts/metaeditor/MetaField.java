package me.bahadir.bsemantix.parts.metaeditor;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NodeMeta;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.hp.hpl.jena.ontology.Individual;

public abstract class MetaField extends Composite {
	
	public static class MetaCardSaveException extends Exception {
		private static final long serialVersionUID = 1070857060573097651L;
		protected MetaField field;
		
		public MetaCardSaveException(MetaField field, String message) {
			super(message);
			this.field = field;
			
		}
		public MetaField getField() {
			return field;
		}

		
		
	}
	
	public static class MandatoryFieldNotFilled extends MetaCardSaveException {
		private static final long serialVersionUID = 3922702021980805075L;
		public MandatoryFieldNotFilled(MetaField field, String message) {
			super(field, message);
			// TODO Auto-generated constructor stub
		}
	};
	
	protected final NodeMeta nodeMeta;
	protected final Individual individual;
	protected String label;
	
	abstract boolean save() throws MetaCardSaveException;
	abstract void load();
	
	
	public MetaField(Composite parent, Individual individual, NodeMeta nodeMeta) {
		super(parent, SWT.None);
		this.nodeMeta = nodeMeta;
		this.individual = individual;
		setLabel();
	}
	public String getLabel() {
		return label;
	}
	
	private void setLabel() {
		this.label = individual == null || nodeMeta == null 
				? "<property>"
				: S.getPropertyCaption(individual.getOntModel(), nodeMeta.getPredicate());
	}
	
	

}
