package me.bahadir.bsemantix.parts;

import me.bahadir.bsemantix.Console;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.SphereNode;

import org.jgrapht.GraphPath;

public class PathModel {

	private GraphPath<SphereNode, NeuralEdge> graphPath;
	private NeuralGraph resolvedPath;
	
	public PathModel(GraphPath<SphereNode, NeuralEdge> graphPath) {
		new Console(PathModel.class);
		this.graphPath = graphPath;

	}

	
	
	public NeuralGraph getResolvedPath() {
		return resolvedPath;
	}



	public GraphPath<SphereNode, NeuralEdge> getGraphPath() {
		return graphPath;
	}

	public void setGraphPath(GraphPath<SphereNode, NeuralEdge> graphPath) {
		this.graphPath = graphPath;
	}
	
	
	
}
