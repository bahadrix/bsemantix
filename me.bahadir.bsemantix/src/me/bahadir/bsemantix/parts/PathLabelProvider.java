package me.bahadir.bsemantix.parts;


import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.parts.SelectionContentProvider.PseudoRootEntity;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PathLabelProvider extends LabelProvider{

	private static final Image SPHERE_ICON = S.getImage("icons/sweet/circle-red-16-ns.png");
	private static final Image EDGE_ICON = S.getImage("icons/sweet/pencil-16-ns.png"); 
	private static final Image PROPERTY_ICON = S.getImage("icons/sweet/note-16-ns.png"); 
	private static final Image PSEUDO_ROOT_ICON = S.getImage("icons/sweet/asset-red-16-ns.png"); 
	
	@Override
	public String getText(Object element) {
		if (element instanceof SphereNode) {
			SphereNode s = (SphereNode) element;
			return s.getName();
		} else if(element instanceof NeuralEdge){
			return ((NeuralEdge) element).getName();
		} else if(element instanceof SelectionContentProvider.Pair){
			SelectionContentProvider.Pair p = (SelectionContentProvider.Pair) element;
			return p.name + ": " + p.value;
		} else if(element instanceof EntitiesListPart.EntitySet) {
			EntitiesListPart.EntitySet pr = (EntitiesListPart.EntitySet) element;
			return pr.title;
		}

		return null;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof SphereNode) {
			return SPHERE_ICON;
		} else if(element instanceof NeuralEdge){
			return EDGE_ICON;
		} else if(element instanceof SelectionContentProvider.Pair){ 
			return PROPERTY_ICON;
		} else if(element instanceof EntitiesListPart.EntitySet) {
			return PSEUDO_ROOT_ICON;
		}
		return null;
		
	}

}
