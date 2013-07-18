package me.bahadir.bsemantix.parts;


import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.SynapticEdge;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class PathLabelProvider extends StyledCellLabelProvider{

	private static final Image SPHERE_ICON = S.getImage("icons/sweet/circle-red-16-ns.png");
	private static final Image EDGE_ICON = S.getImage("icons/sweet/pencil-16-ns.png"); 
	private static final Image PROPERTY_ICON = S.getImage("icons/sweet/note-16-ns.png"); 
	private static final Image PSEUDO_ROOT_ICON = S.getImage("icons/sweet/asset-red-16-ns.png");
	private static final Image SYNAPSE_ICON = S.getImage("icons/net_wrs.png");
	private static final Image PROPERTY_FOLDER_ICON = S.getImage("icons/folder_page_white.png");
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		StyledString text = new StyledString();
		
		if (element instanceof SphereNode) {
			
			SphereNode s = (SphereNode) element;
			
			//cell.setImage(S.getSharedImage(ISharedImages.IMG_OBJ_FOLDER));
			cell.setImage(SPHERE_ICON);
			text.append(s.getName());
		} else if(element instanceof SynapticEdge){
			SynapticEdge se = (SynapticEdge) element;
			cell.setImage(SYNAPSE_ICON);	
			text.append(se.getName());
			//return s.getName();
		} else if(element instanceof NeuralEdge){
			cell.setImage(EDGE_ICON);
			text.append(((NeuralEdge) element).getName());
		} else if(element instanceof TreePair){
			TreePair p = (TreePair) element;
			if(p.isPairArray) {
				cell.setImage(PROPERTY_FOLDER_ICON);
				text.append(p.name);
				//return p.name;
			} else {
				cell.setImage(PROPERTY_ICON);
				text.append(p.name + ": ");
				text.append(p.value, new Styler() {
					
					@Override
					public void applyStyles(TextStyle textStyle) {
						
						textStyle.foreground = new Color(Display.getCurrent(), 74,129,74);

					}
				});
				
			}
		} else if(element instanceof EntitiesListPart.EntitySet) {
			EntitiesListPart.EntitySet pr = (EntitiesListPart.EntitySet) element;
			cell.setImage(PSEUDO_ROOT_ICON);
			text.append(pr.title);
			//return pr.title;
		}
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		
		
		super.update(cell);
	} 
	
	
	
	
	/*
	@Override
	public String getText(Object element) {
		if (element instanceof SphereNode) {
			SphereNode s = (SphereNode) element;
			return s.getName();
		} else if(element instanceof NeuralEdge){
			return ((NeuralEdge) element).getName();
		} else if(element instanceof SelectionContentProvider.Pair){
			SelectionContentProvider.Pair p = (SelectionContentProvider.Pair) element;
			if(p.isPairArray) {
				return p.name;
			} else {
				return p.name + ": " + p.value;
			}
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
		} else if(element instanceof SynapticEdge){
			return SYNAPSE_ICON;
		} else if(element instanceof NeuralEdge){
			return EDGE_ICON;
		} else if(element instanceof SelectionContentProvider.Pair){ 
			SelectionContentProvider.Pair p = (SelectionContentProvider.Pair) element;
			if(p.isPairArray) {
				return PROPERTY_FOLDER_ICON;
			} else {
				return PROPERTY_ICON;
			}
	
		} else if(element instanceof EntitiesListPart.EntitySet) {
			return PSEUDO_ROOT_ICON;
		}
		return null;
		
	}
*/
}
