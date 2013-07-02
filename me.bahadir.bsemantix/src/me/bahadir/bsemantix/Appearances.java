package me.bahadir.bsemantix;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

public class Appearances {
	
	public static final Appearance 
		SolidThinLine,
		GridLine,
		RepulsiveForce;
	
	static {
		SolidThinLine = new Appearance();
	    SolidThinLine.setColoringAttributes(new ColoringAttributes(new Color3f(.7f, .6f, 0),
		        ColoringAttributes.SHADE_GOURAUD));
	    SolidThinLine.setLineAttributes(new LineAttributes(1.5f, LineAttributes.PATTERN_SOLID, true));
	    SolidThinLine.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
	    SolidThinLine.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
	    
	    GridLine = new Appearance();
	    GridLine.setColoringAttributes((new ColoringAttributes(new Color3f(.7f, .6f, .6f),
		        ColoringAttributes.SHADE_GOURAUD)));
	    GridLine.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED,0.7f,
				TransparencyAttributes.BLEND_SRC_ALPHA,	TransparencyAttributes.BLEND_ONE));
	    GridLine.setLineAttributes(new LineAttributes(1.5f, LineAttributes.PATTERN_SOLID, true));
	    GridLine.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
	    GridLine.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
	    
	    RepulsiveForce = new Appearance();
	    RepulsiveForce.setColoringAttributes(new ColoringAttributes(new Color3f(.8f, .4f, 0),
		        ColoringAttributes.SHADE_GOURAUD));
	    RepulsiveForce.setLineAttributes(new LineAttributes(2.5f, LineAttributes.PATTERN_SOLID, true));
	    RepulsiveForce.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
	    RepulsiveForce.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
	}
	
}
