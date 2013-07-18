package me.bahadir.bsemantix.geometry;

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NeuralEdge;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class Pyramid {

	private GeometryArray geometry;
	private Color3f color;
	private Appearance appearance;
	
	public Pyramid() {
		this(new Color3f(1f, .75f, .0f));
	}
	
	public Pyramid(Color3f color) {
		this.color = color;
		Point3d[] points = {
			new Point3d(-1,0,1),
			new Point3d(1,0,1),
			new Point3d(1,0,-1),
			new Point3d(-1,0,-1),
			new Point3d(0,3,0)		
		};
		
		// right hand rule for sorface normals
		int[] mappings = {
			0,3,1,
			1,3,2,
			1,2,4,
			0,1,4,
			0,4,3,
			2,3,4
		};
		
		TriangleArray pyramidGeometry = new TriangleArray(6 * 3, TriangleArray.COORDINATES);
		
		
		for(int i = 0; i < mappings.length; i++) {
			pyramidGeometry.setCoordinate(i, points[mappings[i]]);
		}
		
		GeometryInfo geometryInfo = new GeometryInfo(pyramidGeometry);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geometryInfo);

		this.geometry = geometryInfo.getGeometryArray();

	}

	public void setTransparency(float transparency) {
		appearance.setTransparencyAttributes(
				new TransparencyAttributes(
						TransparencyAttributes.BLENDED,
						transparency,
						TransparencyAttributes.BLEND_SRC_ALPHA,	
						TransparencyAttributes.BLEND_ONE));
	}
	
	public Shape3D createShape() {
		appearance = new Appearance();
		appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
		appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		//Color3f color = new Color3f(.04f, .39f, .64f);


		Texture texture = new Texture2D();
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
		texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		Material mat = new Material(color, S.black, color, S.white, 70f);
		appearance.setTextureAttributes(texAttr);
		appearance.setMaterial(mat);
		appearance.setTexture(texture);
		appearance.setTransparencyAttributes(NeuralEdge.enabledTransparency);
		
		Shape3D shape = new Shape3D(geometry, appearance);
		shape.setPickable(true);

		return shape;
	}
	
	
	
	
}
