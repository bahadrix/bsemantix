package me.bahadir.bsemantix.geometry;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

public class AxisLines {

	private double length = 6.0;
	private Shape3D xShape, yShape, zShape;
	private TransformGroup parent; 
	
	
	
	
	public AxisLines(TransformGroup parent) {
	    
	    LineArray arX =new LineArray(2, LineArray.COORDINATES);
	    arX.setCoordinate(0, new Point3d(0,0,0));
	    arX.setCoordinate(1, new Point3d(length,0,0));
		
	    LineArray arY =new LineArray(2, LineArray.COORDINATES);
	    arY.setCoordinate(0, new Point3d(0,0,0));
	    arY.setCoordinate(1, new Point3d(0,length,0));
	    
	    LineArray arZ =new LineArray(2, LineArray.COORDINATES);
	    arZ.setCoordinate(0, new Point3d(0,0,0));
	    arZ.setCoordinate(1, new Point3d(0,0,length));  
		
		xShape = new Shape3D(arX, getAppearance(new Color3f(1,0,0)));
		yShape = new Shape3D(arY, getAppearance(new Color3f(0,1,0)));
		zShape = new Shape3D(arZ, getAppearance(new Color3f(0,0,1)));
		
		parent.addChild(xShape);
		parent.addChild(yShape);
		parent.addChild(zShape);
		
	}
	
	

	
	private Appearance getAppearance(Color3f color) {
		Appearance appearance = new Appearance();
	    ColoringAttributes ca = new ColoringAttributes(color, ColoringAttributes.SHADE_FLAT);
	    appearance.setColoringAttributes(ca);
	    LineAttributes la = new LineAttributes(1f, LineAttributes.PATTERN_SOLID, true);

	    appearance.setLineAttributes(la);
	    
	    return appearance;
	}
	
}