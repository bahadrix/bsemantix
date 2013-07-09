package me.bahadir.bsemantix;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import me.bahadir.bsemantix.geometry.Pyramid;
import me.bahadir.bsemantix.ngraph.BenchObject;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.SubGraphSet;
import me.bahadir.bsemantix.parts.BenchPart;
import me.bahadir.bsemantix.parts.EntitiesListPart;
import me.bahadir.bsemantix.physics.NodeForcesMap;
import me.bahadir.bsemantix.physics.Physics;
import me.bahadir.bsemantix.semantic.OntoAdapter;
import me.bahadir.bsemantix.semantic.SampleOM;

import org.apache.jena.riot.Lang;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.UndirectedSubgraph;
import org.osgi.framework.BundleContext;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class NeuralBench implements MouseListener, MouseMotionListener {
	protected static Logger log = Logger.getLogger(NeuralBench.class
			.getSimpleName());

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -171827881571413337L;

	public static final String EVENT_SHOW_SHORTEST = "TOPIC_NEURAL_BENCH/SHOW_SHORTEST";

	public enum TransformEvent {
		ZOOM, ROTATE, TRANSLATE
	};

	public final Console console;

	private TransformGroup neuralRoot, vpTransform;
	private Vector3d cubeCenter;
	private final Frame frame;
	private Point3d focalPoint;
	private PickCanvas pickCanvas;
	private BenchObject hoveredBenchObject = null;
	private List<BenchObject> nodeSelection;
	private Point3d orbitOrigin;
	private MouseRotate2 behavior1;
	private MouseWheelZoom behavior2;
	private MouseTranslate behavior3;
	private Dimension screenSize;
	private double rotPerPx;
	private Vector2d mousePos;
	private int pressedButton;

	private NeuralGraph ng;

	private BranchGroup branchGroup;

	private BranchGroup grids;

	private Canvas3D canvas3D;
	
	public NeuralBench(Frame frame) {
		this.console = new Console(NeuralBench.class);
		this.frame = frame;
		this.focalPoint = new Point3d();
		cubeCenter = new Vector3d(focalPoint);
		cubeCenter.setZ(-10);
		orbitOrigin = new Point3d(0, 0, 0);
		cubeCenter = new Vector3d(0, 0, -10);
		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		rotPerPx = (Math.PI / screenSize.height) * 4;
		nodeSelection = new ArrayList<>();
		init();

	}

	public void init() {
		

		
		canvas3D = createCanvas3D();
		SimpleUniverse universe = new SimpleUniverse(canvas3D);
		ViewingPlatform platform = universe.getViewingPlatform();



		
		
//		ng = new NeuralGraph() {
//			private static final long serialVersionUID = 6887255869424217060L;
//
//			@Override
//			public void setOrbitOrigin(Tuple3d tuple) {
//				NeuralBench.this.setOrbitOrigin(tuple);
//				super.setOrbitOrigin(tuple);
//			}
//		};
//
//		SphereNode s0 = new SphereNode("root", new Vector3d(0,0,0));
//		SphereNode s1 = new SphereNode("sag", new Vector3d(1,1,1));
//		ng.addVertex(s0);
//		ng.addVertex(s1);
//		ng.addEdge(new NeuralEdge(s0, s1));

		// fafavafva



		// avafvafvafv

		// @sphere

		Transform3D tr3D = new Transform3D();
		tr3D.rotX(Math.PI / 4.0f);
		tr3D.rotY(Math.PI / 4.0f);
		tr3D.setTranslation(cubeCenter);

		neuralRoot = new TransformGroup(tr3D);
		neuralRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		neuralRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		neuralRoot.setCapability(BranchGroup.ALLOW_DETACH);
		neuralRoot.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		neuralRoot.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		neuralRoot.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		

		Bounds bounds = new BoundingSphere(orbitOrigin, 300);

		// ROTATE
		behavior1 = new MouseRotate2() {

			@Override
			public void transformChanged(Transform3D arg0) {
				NeuralBench.this.transformChanged(arg0, TransformEvent.ROTATE);
				super.transformChanged(arg0);
			}

		};

		behavior1.setSchedulingBounds(bounds);
		behavior1.setFactor(0.008);
		behavior1.setTransformGroup(neuralRoot);

		// neuralRoot.addChild(behavior1);

		// ZOOM
		behavior2 = new MouseWheelZoom() {
			@Override
			public void transformChanged(Transform3D arg0) {
				NeuralBench.this.transformChanged(arg0, TransformEvent.ZOOM);
				super.transformChanged(arg0);
			}
		};

		behavior2.setTransformGroup(neuralRoot);
		behavior2.setFactor(-0.2);
		behavior2.setSchedulingBounds(bounds);

		neuralRoot.addChild(behavior2);

		// TRANSLATE
		behavior3 = new MouseTranslate() {

			@Override
			public void transformChanged(Transform3D arg0) {
				NeuralBench.this.transformChanged(arg0,
						TransformEvent.TRANSLATE);
				super.transformChanged(arg0);
			}

		};

		behavior3.setTransformGroup(neuralRoot);
		neuralRoot.addChild(behavior3);
		behavior3.setSchedulingBounds(bounds);

		branchGroup = new BranchGroup();
		branchGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		branchGroup.setCapability(Group.ALLOW_CHILDREN_READ);
		branchGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);

		branchGroup.addChild(neuralRoot);

		BoundingSphere lightBounds = new BoundingSphere(new Point3d(0.0, 0.0,
				0.0), 1000.0);

		DirectionalLight light = new DirectionalLight(new Color3f(1f, 1f, 1f),
				new Vector3f(5, -6, -30));
		light.setInfluencingBounds(lightBounds);
		branchGroup.addChild(light);

		AmbientLight ambientLight = new AmbientLight(new Color3f(0.2f, 0.2f,
				0.2f));
		ambientLight.setInfluencingBounds(lightBounds);
		branchGroup.addChild(ambientLight);
		// background
		branchGroup.addChild(createBackground());
		branchGroup.compile();

		platform.setNominalViewingTransform();
		universe.addBranchGraph(branchGroup);

		vpTransform = platform.getViewPlatformTransform();
		frame.add(canvas3D, BorderLayout.CENTER);

		View view = universe.getViewer().getView();
		view.setBackClipDistance(3000);
		view.setFrontClipDistance(0.2);

		Vector3d posV = new Vector3d();
		Transform3D vt = new Transform3D();

		vpTransform.getTransform(vt);
		vt.get(posV);

		initGrid();
		pickCanvas = new PickCanvas(canvas3D, branchGroup);
		pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
		pickCanvas
				.setFlags(PickInfo.NODE | PickInfo.CLOSEST_INTERSECTION_POINT);
		pickCanvas.setTolerance(4.0f);
		canvas3D.addMouseListener(this);
		canvas3D.addMouseMotionListener(this);

	}

	public void loadOntology(OntModel model, String nsPrefix) {
		clearArea();
		
		OntoAdapter ontoAdapter = new OntoAdapter(model);
		
		ng = ontoAdapter.createNeuralGraph(nsPrefix);
		ng.setCanvas3D(canvas3D);
		ng.setOriginSetListener(new NeuralGraph.OriginSetListener() {

			@Override
			public void onSetOrbitOrigin(Tuple3d origin) {
				NeuralBench.this.setOrbitOrigin(origin);

			}
		});
		SubGraphSet sgSet = ng.getSubGraphs();
		for (UndirectedSubgraph<SphereNode, NeuralEdge> sg : sgSet.values()) {

			String s = "";

			for (SphereNode sp : sg.vertexSet()) {
				s += sp.getName() + "\r\n";
			}

		}
		ng.render(neuralRoot);
	}

	public void clearArea() {
		
		List<Node> trash = new LinkedList<>();
		
		//Add to recycle bin
		for(int i = 0; i < neuralRoot.numChildren(); i++) {
			Node node = neuralRoot.getChild(i);
			if(node instanceof BranchGroup) {
				trash.add(node);
			}
		}
	
		//empty recycle bin
		for(Node node : trash) {
			neuralRoot.removeChild(node);
		}
		System.gc();
	}
	
	public Canvas3D createCanvas3D() {
		GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();
		gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
		GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices();
		Canvas3D c3d = new Canvas3D(gd[0].getBestConfiguration(gc3D));

		return c3d;
	}

	public void setOrbitOrigin(Tuple3d tuple) {
		// Burada kaldýk
		orbitOrigin = new Point3d(tuple);
		// orbitOrigin.add(getPosition());

		// behavior1.setPivot(new Vector3d(orbitOrigin));
	}

	public Vector3d getPosition() {
		Transform3D t = new Transform3D();
		neuralRoot.getTransform(t);
		Vector3d v = new Vector3d();
		t.get(v);
		return v;
	}

	private void initGrid() {
		grids = new BranchGroup();
		grids.setCapability(BranchGroup.ALLOW_DETACH);
		LineArray lineArr = new LineArray(2, LineArray.COORDINATES);
		for (int i = -20; i < 20; i++) {
			lineArr = new LineArray(2, LineArray.COORDINATES);
			lineArr.setCoordinate(0, new Point3d(i, 20, 0));
			lineArr.setCoordinate(1, new Point3d(i, -20, 0));
			grids.addChild(new Shape3D(lineArr, Appearances.GridLine));

			lineArr = new LineArray(2, LineArray.COORDINATES);
			lineArr.setCoordinate(0, new Point3d(20, i, 0));
			lineArr.setCoordinate(1, new Point3d(-20, i, 0));

			grids.addChild(new Shape3D(lineArr, Appearances.GridLine));
		}
		grids.setPickable(false);
	}

	public void showGrid() {

		neuralRoot.addChild(grids);
	}

	public void hideGrid() {
		neuralRoot.removeChild(grids);
	}

	public BranchGroup createBackground() {
		// create a parent BranchGroup for the Background
		BranchGroup backgroundGroup = new BranchGroup();

		// create a new Background node
		Background back = new Background();

		// set the range of influence of the background
		back.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0),
				10000));

		// load a texture image using the Java 3D texture loader
		TextureLoader texl = null;
		try {
			texl = new TextureLoader(
					new URL(
							"platform:/plugin/me.bahadir.bsemantix/assets/benchback.jpg"),
					"RGB", frame);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		back.setImage(texl.getImage());
		backgroundGroup.addChild(back);

		return backgroundGroup;

	}

	protected void transformChanged(Transform3D transform, TransformEvent e) {
		Vector3d pos = new Vector3d();
		transform.get(pos);
		switch (e) {
		case ROTATE:
			Console.midText(String.format("%s (%.2f, %.2f, %.2f)",
					e.toString(), pos.x, pos.y, pos.z));
			break;
		case TRANSLATE:
			Console.midText(e.toString() + " " + S.Vector3dString(pos));
			break;
		case ZOOM:
			Console.midText(String.format("%s (%.2f)", e.toString(), pos.z));
			break;
		default:
			break;
		}

		ng.onTransformChange(neuralRoot);

	}

	public void onShowShortestPath() {
		console.objectPrintln("Calculating shortest path..");
		if (nodeSelection.size() != 2) {
			Console.println("You must select two nodes");
			return;
		}
		if (nodeSelection.get(0) instanceof SphereNode
				&& nodeSelection.get(1) instanceof SphereNode) {
			GraphPath<SphereNode, NeuralEdge> path = ng.shortestPath(
					(SphereNode) nodeSelection.get(0),
					(SphereNode) nodeSelection.get(1));

			if (path == null) {

				return;
			}

			ng.showPath(path);

			S.broker.post(EntitiesListPart.TOPIC_SET_SELECTION,
					new EntitiesListPart.EntitySet(path, "Path"));
		} else {
			Console.println("You must select two SPHERE node");
		}

	}

	public void disperse(final int forces, final int iteration) {
	
		S.runJob(new Job("AutoDeformJob") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				long time = System.currentTimeMillis();
				monitor.beginTask(String.format(
						"Dispersing started..", iteration),
						iteration);
				for (int i = 1; i <= iteration; i++) {

					log.info("Processing iteration " + i);
					monitor.subTask("Deform iteration: " + i);
					deform(forces, .1f, 5);


					monitor.worked(i);

				}
				log.info(String.format(
							"Dispersing completed in %.2f seconds",
							(float)(System.currentTimeMillis() - time) / 1000
						)
						);
				monitor.done();
				return Status.OK_STATUS;
			}

		});

	}


	public void deform(int forceType, float stepSize, int iteration) {

		for (int i = 0; i < iteration; i++) {
			NodeForcesMap forceMap = ng.getForces(forceType);
			Console.midText("Deforming iteration: " + String.valueOf(i));

			double forceRange = forceMap.getMax() - forceMap.getMin();
			double val;

			for (Vector3d f : forceMap.keySet()) {

				Vector3d temp = new Vector3d();

				SphereNode s = forceMap.get(f);

				val = (f.length() / forceRange) * stepSize;

				temp.normalize(f);
				temp.scale(val);

				s.translateTo(temp);

			}

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!e.isControlDown() && !e.isShiftDown()) {
			clearSelection();
		}
		if (pressedButton == MouseEvent.BUTTON1) {
			pickCanvas.setShapeLocation(e);

			PickInfo result = pickCanvas.pickClosest();
			if (result == null)
				return;
			Node node = result.getNode();

			Node n = node;
			while (n != null) {
				if (n.getName() != null) {
					if (n.getUserData() instanceof BenchObject) {

						BenchObject bo = (BenchObject) n.getUserData();
						bo.onClick();
						addSelection(bo);

					}
				}
				n = n.getParent();
			}
		}

	}

	public void addSelection(BenchObject bo) {
		
		if (!nodeSelection.contains(bo)) {
			if (bo instanceof SphereNode) {
				nodeSelection.add(bo);
				selectionChanged();
			} 
		}

	}

	private void selectionChanged() {
		S.broker.post(EntitiesListPart.TOPIC_SET_SELECTION,
				new EntitiesListPart.EntitySet(nodeSelection, "Selection"));

	}

	public void clearSelection() {
		for (BenchObject bo : nodeSelection) {
			bo.onUnClick();
		}
		nodeSelection.clear();
		ng.enableAll();
		selectionChanged();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePos = new Vector2d(e.getXOnScreen(), e.getYOnScreen());
		pressedButton = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (pressedButton == MouseEvent.BUTTON1) {
			Vector2d delta = new Vector2d(e.getXOnScreen() - mousePos.x,
					e.getYOnScreen() - mousePos.y);
			mousePos.set(e.getXOnScreen(), e.getYOnScreen());

			Transform3D temp = new Transform3D();
			neuralRoot.getTransform(temp);

			Vector3d pivot = new Vector3d(orbitOrigin);
			pivot.negate();

			Transform3D toOrigin = new Transform3D();
			toOrigin.setTranslation(new Vector3d(orbitOrigin));

			Transform3D rotateX = new Transform3D();
			rotateX.rotX(rotPerPx * delta.y);

			Transform3D rotateY = new Transform3D();
			rotateY.rotY(rotPerPx * delta.x);

			Transform3D toMe = new Transform3D();
			toMe.setTranslation(pivot);

			temp.mul(toOrigin);
			temp.mul(rotateX);
			temp.mul(rotateY);
			temp.mul(toMe);

			Vector3d pos = new Vector3d();
			temp.get(pos);

			transformChanged(temp, TransformEvent.ROTATE);
			neuralRoot.setTransform(temp);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pickCanvas.setShapeLocation(e);

		PickInfo result = pickCanvas.pickClosest();
		boolean turnOfCurrent = true;
		BenchObject bo = null;
		if (result != null) {
			Node node = result.getNode();

			Node n = node;
			while (n != null) {
				if (n.getUserData() != null) {
					if (n.getUserData() instanceof BenchObject) {
						bo = (BenchObject) n.getUserData();

						if (bo.equals(hoveredBenchObject)) { // hala ayný yerde
							turnOfCurrent = false;
						} else {
							bo.onHoverIn();
						}
						bo.onHover();
					}
				}

				n = n.getParent();
			}
		}
		if (turnOfCurrent && hoveredBenchObject != null) {
			hoveredBenchObject.onHoverExit();

		}

		hoveredBenchObject = bo;

	}

	public void scaleUp() {
		Transform3D t = new Transform3D();
		neuralRoot.getTransform(t);
		t.setScale(t.getScale() * 1.15f);
		neuralRoot.setTransform(t);

	}

}
