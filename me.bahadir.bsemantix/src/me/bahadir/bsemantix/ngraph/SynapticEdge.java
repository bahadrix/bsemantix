package me.bahadir.bsemantix.ngraph;

import java.util.logging.Logger;

import javax.vecmath.Color3f;



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
