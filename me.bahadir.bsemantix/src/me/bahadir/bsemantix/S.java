package me.bahadir.bsemantix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import me.bahadir.bsemantix.ccortex.CCortex;
import me.bahadir.bsemantix.ngraph.BenchObject;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.parts.PathLabelProvider;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jgrapht.GraphPath;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;

/**
 * Global Static methods
 * 
 * @author Bahadir
 * 
 */
public class S {

	
	
	public static Vector3d unitVectorX = new Vector3d(1, 0, 0);
	public static Vector3d unitVectorY = new Vector3d(0, 1, 0);
	public static Vector3d unitVectorZ = new Vector3d(0, 0, 1);

	public static Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	public static Color3f darkgrey = new Color3f(0.25f, 0.25f, 0.25f);
	public static Color3f grey = new Color3f(0.5f, 0.5f, 0.5f);
	public static Color3f lightgrey = new Color3f(0.75f, 0.75f, 0.75f);
	public static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

	public static IEventBroker broker;
	public static MApplication application;

	
	public static String getPropertyCaption(OntModel om, Property p) {
		if(om != null && p != null) {
			OntProperty op = om.getOntProperty(p.getURI());
			if(op != null && op.getLabel("tr") != null) {
				return op.getLabel("tr"); 
			}
		}
		
		
		if(p.getLocalName() != null){
			return p.getLocalName();
		} else if(p.getURI()!= null){
			return p.getURI();
		} else {
			return "<property>";
		}
			
	}
	
	public static String getIndividualCaption(Individual ind) {
		if(ind.getPropertyValue(DC.title) != null) {
			return ind.getPropertyValue(DC.title).asLiteral().getString(); 
		} else if(ind.getLabel("tr") != null) {
			return ind.getLabel("tr"); 
		} else if(ind.getLocalName() != null){
			return ind.getLocalName();
		} else {
			return "<individual>";
		}
	}
	
	public static String xmlize(String s) {
		return s.replace("<", "").replace(">", "");
	}
	
	public static CCortex getStandartCCortex() {
		return CCortex.getInstance(ProConfig.get(ProConfig.MONGODB_HOST), ProConfig.get(ProConfig.MONGODB_PORT));
	}
	public static Element getFirstElementOfTag(Element e, String...tagNames) {
		
		
		NodeList list = e.getChildNodes();
		for(int i = 0; i < list.getLength(); i++) {
			Object o = list.item(i);
			if(o instanceof Element) {
				Element child = (Element) o;
				for(String tagName : tagNames) {
					if(child.getTagName().equals(tagName)) return child;
				}
						
			}
		}
		
		return null;
	}
	
	public static String implode(String separator, String... data) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < data.length - 1; i++) {
	    //data.length - 1 => to not add separator at the end
	        if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
	            sb.append(data[i]);
	            sb.append(separator);
	        }
	    }
	    sb.append(data[data.length - 1]);
	    return sb.toString();
	}
	
	public static FontData[] extendFontData(FontData[] originalData, int additionalStyle) {
		FontData[] styleData = new FontData[originalData.length];
		for (int i = 0; i < styleData.length; i++) {
			FontData base = originalData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | additionalStyle);
		}
		return styleData;
	}

	public static Color SWTColor(int r, int g, int b) {
		return new Color(Display.getCurrent(), r, g, b);
	}

	public static Image getSharedImage(String symbolicName) {
		return PlatformUI.getWorkbench().getSharedImages().getImage(symbolicName);
	}

	public EModelService getModelService() {
		return (EModelService) S.application.getContext().get(EModelService.class.getName());
	}

	public static void runJob(Job job) {
		setupJob();
		job.schedule();
	}

	public static void setupJob() {
		System.out.println("ko");
		// Setting the progress monitor
		IJobManager manager = Job.getJobManager();
		// EModelService service = (EModelService) BenchPart.application
		// .getContext().get(EModelService.class.getName());

		// ToolItem has the ID "statusbar" in the model
		EModelService service = (EModelService) S.application.getContext().get(EModelService.class.getName());
		MToolControl element = (MToolControl) service.find("me.bahadir.bsemantix.toolcontrol.prgbar", S.application);

		Object widget = element.getObject();
		final IProgressMonitor p = (IProgressMonitor) widget;

		ProgressProvider provider = new ProgressProvider() {
			@Override
			public IProgressMonitor createMonitor(Job job) {
				return p;
			}
		};

		manager.setProgressProvider(provider);
	}

	public static boolean bitTest(int bitset, int flagset) {
		return (bitset & flagset) == flagset;
	}

	public static interface WalkListener {
		public void onObject(Object object);
	}

	public static String Vector3dString(Tuple3d v) {
		return String.format("(%.2f, %.2f, %.2f)", v.x, v.y, v.z);
	}

	public static Vector3d randomVector(double radius) {
		Random rnd = new Random();
		return new Vector3d(rnd.nextDouble() * radius, rnd.nextDouble() * radius, rnd.nextDouble() * radius);
	}

	public static InputStream readFile(URL url) {

		try {
			InputStream inputStream = url.openConnection().getInputStream();
			return inputStream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Image getImage(String file) {
		Bundle bundle = FrameworkUtil.getBundle(PathLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path(file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();

	}

	public static List<BenchObject> getCrumbles(GraphPath<SphereNode, NeuralEdge> path) {
		final List<BenchObject> objects = new LinkedList<>();

		walkPath(path, new WalkListener() {

			@Override
			public void onObject(Object object) {
				objects.add((BenchObject) object);

			}
		});

		return objects;
	}

	public static Point2d get3DTo2DPoint(Canvas3D canvas3D, Point3d point3d) {

		Transform3D temp = new Transform3D();
		canvas3D.getVworldToImagePlate(temp);
		temp.transform(point3d);
		Point2d point2d = new Point2d();
		canvas3D.getPixelLocationFromImagePlate(point3d, point2d);
		return point2d;
	}

	public static void walkPath(GraphPath<SphereNode, NeuralEdge> path, WalkListener listener) {

		SphereNode ls = path.getStartVertex();
		listener.onObject(ls);
		SphereNode nextS = null;
		for (NeuralEdge ne : path.getEdgeList()) {

			nextS = ne.getSourceVertex().equals(ls) ? ne.getTargetVertex() : ne.getSourceVertex();

			listener.onObject(ne);
			listener.onObject(nextS);
			ls = nextS;

		}

		if (!nextS.equals(path.getEndVertex())) {
			Console.println("S> Path not ended as expected!");
		}
	}

	public static BoundingSphere getBoundingSphere(float size) {
		return new BoundingSphere(new Point3d(), size);
	}

	public static byte[] serialize(Object o) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(o);
			return bos.toByteArray();

		} finally {
			out.close();
			bos.close();
		}
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			return in.readObject();

		} finally {
			bis.close();
			in.close();
		}
	}
}
