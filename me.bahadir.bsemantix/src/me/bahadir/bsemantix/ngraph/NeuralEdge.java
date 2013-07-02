package me.bahadir.bsemantix.ngraph;

import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import me.bahadir.bsemantix.Console;
import me.bahadir.bsemantix.parts.SelectionContentProvider.Pair;

import org.jgrapht.graph.DefaultEdge;

import com.hp.hpl.jena.rdf.model.Property;

public class NeuralEdge implements BenchObject{

	private static final long serialVersionUID = -3144594469194969399L;
	public static final TransparencyAttributes enabledTransparency, disabledTransparency;
	
	
	private SphereNode sourceVertex, targetVertex;
	private BranchGroup bLine;
	public NeuralEdge() {};
	private NeuralGraph ng;
	private Shape3D shape;
	
	private Appearance appearance;
	private TransformGroup transformGroup;

	private TransparencyAttributes transAttr;
	private LineArray lineArr;
	private String name = "?";
	private Property property;
	private Point3d midPoint;
	private Point3d point1;
	private Point3d point2;
	
	static {
		enabledTransparency = new TransparencyAttributes(TransparencyAttributes.BLENDED,0.4f,
				TransparencyAttributes.BLEND_SRC_ALPHA,	TransparencyAttributes.BLEND_ONE);
		disabledTransparency = new TransparencyAttributes(TransparencyAttributes.BLENDED,0.9f,
				TransparencyAttributes.BLEND_SRC_ALPHA,	TransparencyAttributes.BLEND_ONE);
	}
	
	public NeuralEdge(SphereNode sourceVertex, SphereNode targetVertex) {
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
		this.transformGroup = new TransformGroup();
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		this.bLine = new BranchGroup();
		bLine.setCapability(BranchGroup.ALLOW_DETACH);
		drawLine();
	}

	
	public void step() {
	
	}
	
	
	public Point3d getMidPoint() {
		return midPoint;
	}



	public void setMidPoint(Point3d midPoint) {
		this.midPoint = midPoint;
	}



	public Point3d getPoint1() {
		return point1;
	}



	public void setPoint1(Point3d point1) {
		this.point1 = point1;
	}



	public Point3d getPoint2() {
		return point2;
	}



	public void setPoint2(Point3d point2) {
		this.point2 = point2;
	}



	public Property getProperty() {
		return property;
	}



	public void setProperty(Property property) {
		this.property = property;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	private void refreshGeoData() {
		point1 = new Point3d();
		point2 = new Point3d();
		midPoint = new Point3d();
		
		lineArr.getCoordinate(0, point1);
		lineArr.getCoordinate(1, point2);
		
		midPoint.add(point1, point2);
		
		midPoint.scale(.5);
	}

	public void redrawForVertex(SphereNode node) {
		int index = sourceVertex.equals(node) ? 0 : 1;
		
		lineArr.setCoordinate(index, new Point3d(node.getPosition()));
		refreshGeoData(); 
	}
	
	private void drawLine() {
		appearance = new Appearance();
	    ColoringAttributes ca = new ColoringAttributes(new Color3f(1, 0.6f, 0),
	        ColoringAttributes.SHADE_GOURAUD);
	    appearance.setColoringAttributes(ca);
	    LineAttributes la = new LineAttributes(3f, LineAttributes.PATTERN_SOLID, true);
	    appearance.setTransparencyAttributes(enabledTransparency);
	    appearance.setLineAttributes(la);
	    appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
	    appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
	    
	    lineArr=new LineArray(2, LineArray.COORDINATES);
	    lineArr.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
		lineArr.setCoordinate(0, new Point3d(sourceVertex.getPosition()));
		lineArr.setCoordinate(1, new Point3d(targetVertex.getPosition()));
		shape = new Shape3D(lineArr, appearance);
		
		shape.setName(NeuralEdge.class.getName());
		shape.setUserData(this);
		shape.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
		transformGroup.addChild(shape);
		refreshGeoData();
	}
	
	public TransformGroup getTransformGroup() {
		return transformGroup;
	}


	public Shape3D getShape() {
		return shape;
	}


	public Appearance getAppearance() {
		return appearance;
	}


	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}


	public void onClick() {
		
	}

	NeuralGraph getNg() {
		return ng;
	}

	void setNg(NeuralGraph ng) {
		this.ng = ng;
	}

	public SphereNode getSourceVertex() {
		return sourceVertex;
	}

	public void setSourceVertex(SphereNode sourceVertex) {
		this.sourceVertex = sourceVertex;
	}

	public SphereNode getTargetVertex() {
		return targetVertex;
	}

	public void setTargetVertex(SphereNode targetVertex) {
		this.targetVertex = targetVertex;
	}


	public void onHover() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onHoverExit() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onHoverIn() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onUnClick() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setTransparency(float alpha) {
		TransparencyAttributes ta = appearance.getTransparencyAttributes();
		ta.setTransparency(alpha);
		
		
	}


	@Override
	public void disable() {
		shape.setPickable(false);
		appearance.setTransparencyAttributes(disabledTransparency);

		
	}


	@Override
	public void enable() {
		shape.setPickable(true);
		appearance.setTransparencyAttributes(enabledTransparency);
		
	}


	@Override
	public List<Pair> getSpecPairs() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onTransformChange(TransformGroup group) {
		// TODO Auto-generated method stub
		
	}


	
	
	
	

}
