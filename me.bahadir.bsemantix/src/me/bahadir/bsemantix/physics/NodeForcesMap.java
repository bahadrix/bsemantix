package me.bahadir.bsemantix.physics;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.vecmath.Vector3d;

import me.bahadir.bsemantix.ngraph.SphereNode;

public class NodeForcesMap extends TreeMap<Vector3d, SphereNode> {

	private static final long serialVersionUID = -8423969846122821174L;
	private double max = 0;
	private double min = Double.MAX_VALUE;

	public static final Comparator<Vector3d> StandartComparator = new Comparator<Vector3d>() {

		@Override
		public int compare(Vector3d o1, Vector3d o2) {
			if (o1.length() < o2.length())
				return +1;
			if (o1.length() > o2.length())
				return -1;
			return 0;
		}
	};

	public NodeForcesMap(Comparator<? super Vector3d> comparator) {
		super(comparator);
		// TODO Auto-generated constructor stub
	}

	public NodeForcesMap(Map<? extends Vector3d, ? extends SphereNode> m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	public NodeForcesMap(SortedMap<Vector3d, ? extends SphereNode> m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	public void drawAllNormals() {
		for (SphereNode s : values()) {
			s.showRepulsiveVectorNormal();
		}

	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	@Override
	public SphereNode put(Vector3d key, SphereNode value) {
		double len = key.length();
		if (len > max)
			max = len;
		if (len < min)
			min = len;
		return super.put(key, value);
	}

}
