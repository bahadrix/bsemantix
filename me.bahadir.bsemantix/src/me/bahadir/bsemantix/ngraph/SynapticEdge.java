package me.bahadir.bsemantix.ngraph;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.vecmath.Color3f;

import org.apache.jena.atlas.logging.Log;

import com.hp.hpl.jena.rdf.model.Property;

import me.bahadir.bsemantix.parts.TreePair;
import me.bahadir.bsemantix.semantic.SynapticProperty;



public class SynapticEdge extends RestrictedEdge {


	/**
	 * 
	 */
	private static final long serialVersionUID = 462494079989861581L;
	protected static Logger log = Logger.getLogger(SynapticEdge.class.getSimpleName());

	
	public SynapticEdge(NeuralGraph ng, Restrict rest) {
		super(ng, rest);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void init() {
		this.pyramidColor = new Color3f(.8f,0f,.45f);
		this.lineColor = new Color3f(.8f,0f,.45f);

	}


	
	
	

}
