package me.bahadir.bsemantix.ngraph;

import javax.media.j3d.Material;
import javax.vecmath.Color3f;

import me.bahadir.bsemantix.S;

public class SphereMaterials {
	//public static final SphereMaterials SHELL_RED = new SphereMaterials(new Color3f(.8f, .15f, .15f), new Color3f(0, .8f, 0));
	//public static final SphereMaterials SHELL_GREEN = new SphereMaterials(new Color3f(.36f, .88f, .0f), new Color3f(0, .8f, 0));
		//new Color3f(.3f, .10f, .10f)
	public static SphereMaterial PISTACHIO = new SphereMaterial(
			new Material(new Color3f(.3f, .10f, .10f), S.black, new Color3f(0.72f, .95f, .0f), new Color3f(.1f, .1f, .1f), .7f), 
			new Material(new Color3f(.8f, .15f, .15f), S.black, new Color3f(.8f, .15f, .15f), new Color3f(0, .8f, 0), 1.0f));
	
	public static SphereMaterial GREEN = new SphereMaterial(
			new Material(new Color3f(.1f, 0.7f, .0f), S.black, new Color3f(.1f, 0.7f, .0f), new Color3f(.1f, .1f, .1f), .7f), 
			new Material(new Color3f(.1f, 0.4f, .0f), S.black, new Color3f(.1f, 0.4f, .0f), new Color3f(0, .6f, 0), 1.0f));
	
	public static SphereMaterial RED = new SphereMaterial(
			new Material(new Color3f(1f, 0.29f, .0f), S.black, new Color3f(1f, 0.29f, .0f), new Color3f(.1f, .1f, .1f), .7f), 
			new Material(new Color3f(1f, 0.29f, .0f), S.black, new Color3f(1f, 0.29f, .0f), new Color3f(0, .6f, 0), 1.0f));
	
	
	
	public static class SphereMaterial {
		public final Material core;
		public final Material shell;
		public SphereMaterial(Material core, Material shell) {
			this.core = core;
			this.shell = shell;
		}
		
	}

	
}
