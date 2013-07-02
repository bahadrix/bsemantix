package me.bahadir.bsemantix.ngraph;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.graph.UndirectedSubgraph;

public class SubGraphSet extends LinkedHashMap<Integer, UndirectedSubgraph<SphereNode, NeuralEdge>>{

	private static final long serialVersionUID = -7672994164210200441L;

	public SubGraphSet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubGraphSet(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
		// TODO Auto-generated constructor stub
	}

	public SubGraphSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		// TODO Auto-generated constructor stub
	}

	public SubGraphSet(int initialCapacity) {
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	public SubGraphSet(Map<? extends Integer, ? extends UndirectedSubgraph<SphereNode, NeuralEdge>> m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	
	

}
