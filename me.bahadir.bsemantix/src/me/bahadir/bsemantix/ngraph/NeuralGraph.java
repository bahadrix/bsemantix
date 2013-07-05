package me.bahadir.bsemantix.ngraph;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import me.bahadir.bsemantix.Console;
import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.physics.NodeForcesMap;
import me.bahadir.bsemantix.physics.Physics;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.UndirectedSubgraph;

public class NeuralGraph extends SimpleGraph<SphereNode, NeuralEdge>{

	protected static Logger log = Logger.getLogger(NeuralGraph.class.getSimpleName());

	private static final long serialVersionUID = 3785789143983992281L;
	private SphereNode selectedNode;
	private Console console;
	private Canvas3D canvas3D;
	private String nsPrefix;
	
	public interface OriginSetListener {
		public void onSetOrbitOrigin(Tuple3d origin);
	}
	private OriginSetListener originSetListener;
	
	public NeuralGraph() {
		super(NeuralEdge.class);
		console = new Console(NeuralGraph.class);
	}

	public SubGraphSet getSubGraphs() {
		SubGraphSet subset = new SubGraphSet();
		
		ConnectivityInspector<SphereNode, NeuralEdge> ci = new ConnectivityInspector<>(this);
		List<SphereNode> nodes = new LinkedList<>(vertexSet());
		
		Iterator<SphereNode> it = nodes.iterator();
		Set<SphereNode> addedSet = new HashSet<>();
		int i = 0;
		Set<SphereNode> singleNodes = new HashSet<>();
		
		while(it.hasNext()) {
			i++;
			
			
			SphereNode s = it.next();
			if(addedSet.contains(s)) continue;
			
			Set<SphereNode> sphereSet = ci.connectedSetOf(s);
			Set<NeuralEdge> edgeSet = new HashSet<>();
			
			for(SphereNode sp : sphereSet) {
				if(addedSet.contains(sp)) continue;
				
				addedSet.add(sp);
				for(NeuralEdge e : edgesOf(sp)) {
					edgeSet.add(e);
				}
				
			}
			
			if(sphereSet.size() == 1) {
				singleNodes.addAll(sphereSet);
			} else {
				subset.put(i, new UndirectedSubgraph<>(this, sphereSet, edgeSet));
			}
			
			
			
		}
		
		subset.put(0, new UndirectedSubgraph<>(this, singleNodes, new HashSet<NeuralEdge>()));
		
		
		
		return subset;
	}
	
	public void setOriginSetListener(OriginSetListener originSetListener) {
		this.originSetListener = originSetListener;
	}



	public Canvas3D getCanvas3D() {
		return canvas3D;
	}

	public void setCanvas3D(Canvas3D canvas3d) {
		canvas3D = canvas3d;
	}

	public GraphPath<SphereNode, NeuralEdge> shortestPath(SphereNode n1, SphereNode n2) {
		console.objectPrintln("Running Dijkstra Shortest path");
		long time = System.currentTimeMillis();
		DijkstraShortestPath<SphereNode, NeuralEdge> dsp = new DijkstraShortestPath<SphereNode, NeuralEdge>(this, n1, n2);
		
		if(dsp.getPath() == null) {
			log.warning("No path found");
			return null;
		} else {

			log.info(String.format("Found path with length %.2f in %s miliseconds.",dsp.getPathLength(), System.currentTimeMillis() - time));
		
			
			return dsp.getPath();
		}
	}
	
	public void showPath(List<NeuralEdge> edges) {
		disableAll();
		for(NeuralEdge edge : edges) {
			edge.enable();
			edge.getSourceVertex().enable();
			edge.getTargetVertex().enable();
		}
	}
	
	public void showPath(GraphPath<SphereNode, NeuralEdge> graphPath) {

			showPath(graphPath.getEdgeList());

	}
	
	public void semiTransparentAll() {
		for(BenchObject bo : vertexSet()) {
			bo.setTransparency(0.6f);
		}
		for(BenchObject bo : edgeSet()) {
			bo.setTransparency(0.6f);
		}
	}
	
	public void disableAll() {
		for(BenchObject bo : vertexSet()) {
			bo.disable();
		}
		for(BenchObject bo : edgeSet()) {
			bo.disable();
		}
	}
	
	public void enableAll() {
		for(BenchObject bo : vertexSet()) {
			bo.enable();
		}
		for(BenchObject bo : edgeSet()) {
			bo.enable();
		}
	}
	
	public SphereNode getSelectedNode() {
		return selectedNode;
	}



	public void setSelectedNode(SphereNode selectedNode) {
		this.selectedNode = selectedNode;
	}


	public void onTransformChange(TransformGroup group) {
		
		for(NeuralEdge edge : edgeSet()) {
			edge.onTransformChange(group);
		}
		
		for(SphereNode s : vertexSet()) {
			s.onTransformChange(group);
		}
		
	}
	
	@Override
	public boolean addVertex(SphereNode v) {
		v.setRootGraph(this);
		if(this.vertexSet().size() == 0) v.onClick();
		return super.addVertex(v);
	}

	public void render(TransformGroup tg) {
		
		for(SphereNode sn : this.vertexSet()) {
			tg.addChild(sn.getTransformGroup());
		}
		
		for(NeuralEdge ne : this.edgeSet()) {
			tg.addChild(ne.getTransformGroup());
		}
	}
	
	public NodeForcesMap getForces(int forces) {

		NodeForcesMap forceMap = new NodeForcesMap(NodeForcesMap.StandartComparator);
		

		Console.midText("Calculating force map");
		
		for(SphereNode s : vertexSet()) {
			Vector3d v = new Vector3d();
			if(S.bitTest(forces, Physics.FORCE_REPULSIVE) ) {
				v.add(s.getRepulisiveForce());
			}
			if(S.bitTest(forces, Physics.FORCE_LONGING)) {
				v.add(s.getLongingForce());
			}
			forceMap.put(v, s);
			
		}

		
		return forceMap;
	}
	
	public void onClick() {
		Console.println("Line clicked");
	}
	

	/**
	 * Override for setting rotation orbit center
	 * @param tuple
	 */
	public void setOrbitOrigin(Tuple3d tuple) {
		if(originSetListener != null) originSetListener.onSetOrbitOrigin(tuple);
	}

	@Override
	public NeuralEdge addEdge(SphereNode sourceVertex, SphereNode targetVertex) {
		NeuralEdge ne = new NeuralEdge(sourceVertex, targetVertex);
		addEdge(sourceVertex, targetVertex, ne);
		return ne;
	}

	public void addEdge(NeuralEdge edge) {
		addEdge(edge.getSourceVertex(), edge.getTargetVertex(), edge);
	}
	
	@Override
	public boolean addEdge(SphereNode sourceVertex, SphereNode targetVertex,
			NeuralEdge e) {
		e.setNg(this);
		return super.addEdge(sourceVertex, targetVertex, e);
	}

	public String getNsPrefix() {
		return nsPrefix;
	}

	public void setNsPrefix(String nsPrefix) {
		this.nsPrefix = nsPrefix;
	}

	
	
	
}
