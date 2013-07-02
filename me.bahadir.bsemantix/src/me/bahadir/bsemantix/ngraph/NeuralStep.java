package me.bahadir.bsemantix.ngraph;


public class NeuralStep {

	public final SphereNode endPoint;
	public final NeuralEdge edge;

	public NeuralStep(SphereNode endPoint, NeuralEdge edge) {
		this.endPoint = endPoint;
		this.edge = edge;
	}

}
