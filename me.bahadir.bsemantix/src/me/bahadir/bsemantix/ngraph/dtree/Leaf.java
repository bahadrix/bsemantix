package me.bahadir.bsemantix.ngraph.dtree;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.SphereNode;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;
import org.w3c.dom.Element;

public class Leaf extends GraphNode implements Serializable{


	private static final long serialVersionUID = -2649898462705767113L;
	public static enum LeafType {OUTPUT, BLOCK};
	
	// This data saved to file for completly restore object
	@XmlRootElement(name="leaf")
	public static class LeafData extends JAXNode{
		public final static String blockText = "*";
		@XmlAttribute(name="leaftype") public final LeafType type;
		@XmlAttribute private String outputUri = null;
		@XmlAttribute private String text;
		@XmlAttribute private int locationX = 0;
		@XmlAttribute private int locationY = 0;
		
		public LeafData() {
			this.type = LeafType.BLOCK;
			this.text = blockText;
		}
		public LeafData(String outputUri, String text) {
			this.type = LeafType.OUTPUT;
			this.outputUri = outputUri;
			this.text = text;
		}
		public static final LeafData createFromElement(Element leafElement) {
			
			LeafData data;
			String uri = leafElement.getAttribute("uri");
			
			
			if(uri.equals("block")) {
				data = new LeafData();
			} else {
				data = new LeafData(uri, leafElement.getAttribute("text"));
			}
			
//			data.location = new Point(
//					Integer.parseInt(leafElement.getAttribute("locationX")), 
//					Integer.parseInt(leafElement.getAttribute("locationY")));
			
			return data;
		}
	}
	
	private LeafData lData;
	
	
	
	public final LeafData getLeafData() {
		lData.locationX = getLocation().x;
		lData.locationY = getLocation().y;
		return lData;
	}

	public static Leaf createBlockLeaf(DecisionTree decisionTree) {
		Leaf blockLeaf = new Leaf(decisionTree, new LeafData());
		return blockLeaf;
	}
	
	public Leaf(DecisionTree decisionTree, LeafData lData) {
		super(decisionTree, SWT.None);
		this.lData = lData;
		setLocation(lData.locationX, lData.locationY);
		setText(lData.text);
		setMyStyle();
	}


	
	private void setMyStyle() {
		switch (lData.type) {
		case OUTPUT:
			setForegroundColor(S.SWTColor(0, 0, 0));
			setBackgroundColor(S.SWTColor(147, 181, 45));
			setBorderColor(S.SWTColor(117, 157, 0));
			setHighlightColor(S.SWTColor(180, 242, 0));
			setBorderHighlightColor(S.SWTColor(147, 181, 45));
			break;
		case BLOCK:
			setForegroundColor(S.SWTColor(255, 255, 255));
			setBackgroundColor(S.SWTColor(200, 113, 48));
			setHighlightColor(S.SWTColor(255, 116, 0));
			setBorderHighlightColor(S.SWTColor(255, 150, 64));
			break;
		}

	}
}