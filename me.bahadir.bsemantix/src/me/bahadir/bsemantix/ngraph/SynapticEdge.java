package me.bahadir.bsemantix.ngraph;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.vecmath.Color3f;

import org.apache.jena.atlas.logging.Log;

import me.bahadir.bsemantix.parts.TreePair;



public class SynapticEdge extends NeuralEdge {
	protected static Logger log = Logger.getLogger(SynapticEdge.class.getSimpleName());
	private Synapse synapse;
	
	public SynapticEdge(NeuralGraph ng, Synapse synapse) {
		super(
				ng.getNodeByUri(synapse.getSource().getURI()), 
				ng.getNodeByUri(synapse.getRange().getURI()));
		setNg(ng);
		setName(String.format("syn:%s", synapse.getProperty().getOwlProperty().getLocalName()));

		this.synapse = synapse;
	}

	@Override
	protected void drawLine() {
		this.pyramidColor = new Color3f(.8f,0f,.45f);
		this.lineColor = new Color3f(.8f,0f,.45f);
		super.drawLine();
	}

	
	
	@Override
	public List<TreePair> getSpecPairs() {
		List<TreePair> pairs = new LinkedList<>();
		
		pairs.add(new TreePair("Property URI", synapse.getProperty().getOwlProperty().getURI()));
		
		//Cardinality Label
		
		String cardinalityLabel = "Cardinality";
		String exactCardLabel = synapse.getExactCardinality() == Synapse.STAR_CARDINAL ? "*" : String.valueOf(synapse.getExactCardinality());
		String minCardLabel = synapse.getMinCardinality() == Synapse.STAR_CARDINAL ? "*" : String.valueOf(synapse.getMinCardinality());
		String maxCardLabel = synapse.getMaxCardinality() == Synapse.STAR_CARDINAL ? "*" : String.valueOf(synapse.getMaxCardinality());
		
		if(synapse.getExactCardinality() != Synapse.STAR_CARDINAL) {
			cardinalityLabel += String.format(" (exact %d)", synapse.getExactCardinality());
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
