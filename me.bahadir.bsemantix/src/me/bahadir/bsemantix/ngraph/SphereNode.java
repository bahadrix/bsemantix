package me.bahadir.bsemantix.ngraph;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.apache.jena.atlas.logging.Log;

import me.bahadir.bsemantix.Appearances;
import me.bahadir.bsemantix.Console;
import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.SphereMaterials.SphereMaterial;
import me.bahadir.bsemantix.parts.TreePair;
import me.bahadir.bsemantix.physics.Physics;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;

public class SphereNode implements BenchObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3945818628723369739L;
	protected static Logger log = Logger.getLogger(SphereNode.class.getSimpleName());
	public static float RADIUS = 0.1f;
	private static float FOCUS_SPHERE_RADIUS = 0.13f;
	
	public enum ResourceType {CLASS, INDIVIDUAL};
	private Appearance ap, focusAp;
	private TransformGroup transformGroup;
	private Vector3d position;
	private NeuralGraph rootGraph;
	private boolean selected = false;
	private TransparencyAttributes transAttr;
	private String name;
	private TransparencyAttributes transAttrInner;
	private Sphere sphere;
	private Sphere focusSphere;
	private List<BranchGroup> additions;
	private BranchGroup drawingRepulsiveVector;
	private OntClass ontClass = null;
	private Individual individual = null;
	private SphereMaterial sphereMaterial;
	public final ResourceType resourceType;
	private TransformGroup labelTransform;
	

	public SphereNode(String name, Vector3d position, OntClass cls) {
		this(name, position, ResourceType.CLASS);
		setOntClass(cls);
		
	}
	public SphereNode(String name, Vector3d position, Individual ind) {
		this(name, position, ResourceType.INDIVIDUAL);
		setIndividual(ind);
		
	}
	
	private SphereNode(String name, Vector3d position, ResourceType type) {
		this.additions = new LinkedList<>();
		this.position = position;
		this.resourceType = type;
		this.name = name == null ? "<unknown>" : name;
		
		switch(type) {
		case CLASS:
			this.sphereMaterial = SphereMaterials.RED;
			break;
		case INDIVIDUAL:
			this.sphereMaterial = SphereMaterials.GREEN;
			break;
		}
		
		this.transformGroup = new TransformGroup();

		transformGroup.setCapability(BranchGroup.ALLOW_DETACH);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		transformGroup.setName(getClass().getName());
		ap = new Appearance();
		ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
		ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		ap.setMaterial(sphereMaterial.core);
		ap.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		ap.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
		transAttrInner = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.0f, TransparencyAttributes.BLEND_SRC_ALPHA,
				TransparencyAttributes.BLEND_ONE);
		transAttrInner.setTransparencyMode(TransparencyAttributes.NONE);
		transAttrInner.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		transAttrInner.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
		ap.setTransparencyAttributes(transAttrInner);

		focusAp = new Appearance();
		focusAp.setCapability(Appearance.ALLOW_MATERIAL_READ);
		focusAp.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

		focusAp.setMaterial(sphereMaterial.shell);
		transAttr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.5f, TransparencyAttributes.BLEND_SRC_ALPHA,
				TransparencyAttributes.BLEND_ONE);
		transAttr.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		focusAp.setTransparencyAttributes(transAttr);

		transAttr.setTransparency(1);

		Transform3D transform = new Transform3D();
		transform.setTranslation(position);

		transformGroup.setTransform(transform);

		sphere = new Sphere(RADIUS, Sphere.GENERATE_NORMALS, 40, ap);
		sphere.setCapability(Sphere.ALLOW_PICKABLE_WRITE);
		focusSphere = new Sphere(FOCUS_SPHERE_RADIUS, Sphere.GENERATE_NORMALS, 40, focusAp);
		focusSphere.setPickable(false);

		
		Text2D text2d = new Text2D(this.name, new Color3f(0.9f, 1.0f, 1.0f), "Helvetica", 24, Font.ITALIC);
		labelTransform = new TransformGroup();
		labelTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		labelTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		labelTransform.addChild(text2d);
		
		
		transformGroup.addChild(labelTransform);
		transformGroup.addChild(sphere);
		transformGroup.addChild(focusSphere);
		transformGroup.setUserData(this);
	}

	public String getUri() {
		switch(resourceType) {
		case CLASS:
			return getOntClass().getURI();
		case INDIVIDUAL:
			return getIndividual().getURI();
		default:
			return null;
		}
	}

	public Individual getIndividual() {
		return individual;
	}

	
	
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	public OntClass getOntClass() {
		return ontClass;
	}

	public void setOntClass(OntClass ontClass) {
		this.ontClass = ontClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public NeuralGraph getRootGraph() {
		return rootGraph;
	}

	void setRootGraph(NeuralGraph rootGraph) {
		this.rootGraph = rootGraph;
	}

	public void onClick() {

		select();

	}

	public double getMass(int forces) {
		return 1;
	}
	
	public void translateTo(final Vector3d translationVector) {

		Vector3d newPos = new Vector3d();
		newPos.add(getPosition(), translationVector);

		Transform3D tr = getTransform();
		tr.setTranslation(newPos);

		setTransform(tr);

		Set<NeuralEdge> edges = rootGraph.edgesOf(this);
		for (NeuralEdge edge : edges) {
			edge.redrawForVertex(this);
		}


	}

	public TransformGroup getTransformGroup() {
		return transformGroup;
	}

	public Vector3d getPosition() {
		getTransform().get(position);
		return position;
	}

	public void select() {
		selected = true;

		rootGraph.setOrbitOrigin(getPosition());
		rootGraph.setSelectedNode(this);
		drawSelect();
		showLongingVectorNormal();
	}

	public void unselect() {
		selected = false;
		undrawSelect();
		clearAdditions();
	}

	public Vector3d getRepulisiveForce() {

		Vector3d v = new Vector3d();
		for (SphereNode s : rootGraph.vertexSet()) {
			if (!s.equals(this)) {
				v.add(Physics.repulsiveVector(this, s));
			}
		}
		
		for (NeuralEdge e : rootGraph.edgeSet()) {
			v.add(Physics.repulsiveVector(this, e));
		}
		return v;
	}

	public Vector3d getLongingForce() {
		Vector3d v = new Vector3d();
		for (SphereNode s : rootGraph.vertexSet()) {
			if (!s.equals(this)) {
				v.add(Physics.longingVector(this, s));
			}
		}

		for (NeuralEdge e : rootGraph.edgeSet()) {
			v.add(Physics.longingVector(this, e));
		}
		return v;
	}
	
	
	
	public void showRepulsiveVectorNormal() {
		Vector3d rvNormal = new Vector3d();
		rvNormal.normalize(getRepulisiveForce());
		drawVectorOn(rvNormal);
	}

	public void showLongingVectorNormal() {
		Vector3d lvNormal = new Vector3d();
		lvNormal.normalize(getLongingForce());
		drawVectorOn(lvNormal);
	}
	
	public void clearAdditions() {
		for (BranchGroup bg : additions) {
			transformGroup.removeChild(bg);
		}
	}

	public BranchGroup drawVectorOn(Vector3d v1) {


		LineArray lineArr = new LineArray(2, LineArray.COORDINATES);

		lineArr.setCoordinate(0, new Point3d());
		lineArr.setCoordinate(1, new Point3d(v1));
		Shape3D shape = new Shape3D(lineArr, Appearances.RepulsiveForce);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setPickable(false);
		bg.addChild(shape);
		transformGroup.addChild(bg);

		additions.add(bg);

		return bg;
	}

	public void removeVectorDrawing(BranchGroup bg) {

		if (additions.contains(bg)) {
			transformGroup.removeChild(bg);
			additions.remove(bg);
		}

	}

	public void setPosition(Vector3d position) {
		Transform3D transform = getTransform();
		transform.setTranslation(position);
		setTransform(transform);
		this.position = position;
	}

	/**
	 * Returns clone, not the reference.
	 * 
	 * @return Clone of transform group transform
	 */
	public Transform3D getTransform() {
		Transform3D tr3d = new Transform3D();
		getTransformGroup().getTransform(tr3d);
		return tr3d;
	}

	public void setTransform(Transform3D tr3d) {
		getTransformGroup().setTransform(tr3d);
	}

	public void onHover() {
		if(rootGraph.getCanvas3D() != null) {
			Console.midText(
					S.get3DTo2DPoint(
							rootGraph.getCanvas3D(), 
							new Point3d(getPosition())
							).toString()
					);
		} else {
			log.severe("Canvas not set for node");
		}
	}

	@Override
	public void onHoverExit() {
		if (!selected)
			undrawSelect();
	}

	@Override
	public void onHoverIn() {
		drawSelect();
	}

	private void drawSelect() {
		transAttr.setTransparency(0.5f);
	}

	private void undrawSelect() {
		transAttr.setTransparency(1f);
	}

	@Override
	public void onUnClick() {
		unselect();

	}

	@Override
	public void setTransparency(float alpha) {
		// transAttrInner.setTransparency(alpha);
		transAttrInner.setTransparency(alpha);
		if (alpha == 0f) {
			transAttrInner.setTransparencyMode(TransparencyAttributes.NONE);
		} else {
			transAttrInner.setTransparencyMode(TransparencyAttributes.BLENDED);
		}

	}

	@Override
	public void disable() {
		setTransparency(0.7f);
		sphere.setPickable(false);
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		setTransparency(0);
		sphere.setPickable(true);
	}

	@Override
	public List<TreePair> getSpecPairs() {
		List<TreePair> pairs = new LinkedList<>();

		pairs.add(new TreePair("Name", getName()));
		pairs.add(new TreePair("Position", S.Vector3dString(getPosition())));
		pairs.add(new TreePair("Repulsive FV", S.Vector3dString(getRepulisiveForce())));
		
		switch (resourceType) {
		case CLASS:
			pairs.add(new TreePair("URI", ontClass.getURI()));
			break;
		case INDIVIDUAL:
			pairs.add(new TreePair("URI", individual.getURI()));
			break;
		}
		if(ontClass != null) {
			
		};
	
		return pairs;
	}

	@Override
	public void onTransformChange(TransformGroup group) {
		
	
	}

}
