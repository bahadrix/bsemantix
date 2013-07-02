package me.bahadir.bsemantix.parts;

import java.util.ArrayList;
import java.util.List;

import me.bahadir.bsemantix.Console;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.NeuralStep;
import me.bahadir.bsemantix.ngraph.SphereNode;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class PathModel {

	private GraphPath<SphereNode, NeuralEdge> graphPath;
	private NeuralGraph resolvedPath;
	private Console console;
	

	public PathModel(GraphPath<SphereNode, NeuralEdge> graphPath) {
		this.console = new Console(PathModel.class);
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
