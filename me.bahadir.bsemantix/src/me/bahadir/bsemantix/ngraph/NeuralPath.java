package me.bahadir.bsemantix.ngraph;

import java.util.List;

public class NeuralPath {

	private List<Step> steps;
	private List<SphereNode> points;
	public class Step {
		public final SphereNode endPoint;
		public final NeuralEdge edge;
		public Step(SphereNode endPoint, NeuralEdge edge) {
			this.endPoint = endPoint;
			this.edge = edge;
		}
		
	}
	
	public NeuralPath(List<NeuralEdge> edges, SphereNode startPoint) {
		
	}
	
	
}
