package me.bahadir.bsemantix.parts;

import java.util.LinkedList;
import java.util.List;

import me.bahadir.bsemantix.ngraph.BenchObject;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.parts.EntitiesListPart.EntitySet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SelectionContentProvider implements ITreeContentProvider {

	private EntitySet[] entitySets;

	public static class Pair {
		public final String name;
		public final String value;

		public Pair(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}

	public static class PseudoRootEntity {
		public final String name;

		public PseudoRootEntity(String name) {
			this.name = name;
		}

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		this.entitySets = (EntitySet[]) newInput;

	}

	@Override
	public Object[] getElements(Object inputElement) {

		return entitySets;
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof EntitySet) {
			EntitySet entitySet = (EntitySet) parentElement;
			return entitySet.selection.toArray();
		} else if (parentElement instanceof BenchObject) {
				BenchObject bo = (BenchObject) parentElement;
				return bo.getSpecPairs().toArray();
		}
		
		return null;
		
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof SphereNode) return true;
		if (element instanceof EntitiesListPart.EntitySet) return true;
		return false;
	}

}
