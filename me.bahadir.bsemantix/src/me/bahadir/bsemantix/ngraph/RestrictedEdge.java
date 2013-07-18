package me.bahadir.bsemantix.ngraph;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.vecmath.Color3f;

import me.bahadir.bsemantix.parts.TreePair;

import com.hp.hpl.jena.rdf.model.Property;

public class RestrictedEdge extends NeuralEdge{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3573456923335915407L;
	protected static Logger log = Logger.getLogger(RestrictedEdge.class.getSimpleName());
	protected Restrict rest;
	
	public RestrictedEdge(NeuralGraph ng, Restrict rest) {
		super(
				ng.getNodeByUri(rest.getSource().getURI()), 
				ng.getNodeByUri(rest.getRange().getURI()));
		setNg(ng);
		setName(String.format("%s", rest.getPredicate().getLocalName()));
		
		this.rest = rest;
	}
	
	@Override
	protected void init() {
		this.pyramidColor = new Color3f(.58f,.71f,.18f);
		this.lineColor = new Color3f(.58f,.71f,.18f);
	}
	
	@Override
	public Property getProperty() {
	
		return rest.getPredicate();
	}

	@Override
	public List<TreePair> getSpecPairs() {
		List<TreePair> pairs = new LinkedList<>();
		
		pairs.add(new TreePair("Property URI", rest.getPredicate().getURI()));
		
		//Cardinality Label
		
		String cardinalityLabel = "Cardinality";
		String exactCardLabel = rest.getExactCardinality() == Restrict.STAR_CARDINAL ? "*" : String.valueOf(rest.getExactCardinality());
		String minCardLabel = rest.getMinCardinality() == Restrict.STAR_CARDINAL ? "*" : String.valueOf(rest.getMinCardinality());
		String maxCardLabel = rest.getMaxCardinality() == Restrict.STAR_CARDINAL ? "*" : String.valueOf(rest.getMaxCardinality());
		
		if(rest.getExactCardinality() != Restrict.STAR_CARDINAL) {
			cardinalityLabel += String.format(" (exact %d)", rest.getExactCardinality());
		} else {
			cardinalityLabel += String.format(" (%s..%s)", minCardLabel, maxCardLabel);
		}
		
		pairs.add(new TreePair(cardinalityLabel, new TreePair[] {
				new TreePair("Min",minCardLabel),
				new TreePair("Max",maxCardLabel),
				new TreePair("Exact",exactCardLabel)
				
		}));
		
		pairs.add(new TreePair("Decision Model","<none>"){

			@Override
			public void onDoubleClicked() {
				log.info("lets rock some decision");
				super.onDoubleClicked();
			}
			
		});
		
		pairs.addAll(super.getSpecPairs());
		return pairs;
	}
	
	
}
