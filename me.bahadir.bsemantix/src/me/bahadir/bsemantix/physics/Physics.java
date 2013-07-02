package me.bahadir.bsemantix.physics;

import javax.vecmath.Vector3d;

import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.SphereNode;

public class Physics {

	// Force Types
	public static final int FORCE_ALL = Integer.MAX_VALUE;
	public static final int FORCE_REPULSIVE = 1;
	public static final int FORCE_LONGING = 2;
	public static final int FORCE_WITH_EDGE = 4;
	// Force Presets
	public static final int FORCE_SPHERE_DISPERSE = FORCE_REPULSIVE | FORCE_REPULSIVE | FORCE_WITH_EDGE;
	
	public static final float G = 1;
	/**
	 * iki obje biribine ne kadar yakýnsa birbilerini o kadar çok iterler
	 * @param node
	 * @param repulser
	 * @return
	 */
	public static Vector3d repulsiveVector(SphereNode node, SphereNode repulser) {
		Vector3d Vi;
		Vector3d Vm1 = new Vector3d(node.getPosition());
		Vector3d Vm2 = new Vector3d(repulser.getPosition());

		double m1 = node.getMass(FORCE_REPULSIVE);
		double m2 = repulser.getMass(FORCE_REPULSIVE);
 
		
		Vector3d Vd = new Vector3d();
		Vd.sub(Vm1, Vm2);
		
		double vd3 = Math.pow(Vd.length(), 3);
		
		Vi = new Vector3d(Vd);
		Vi.scale((G*m1*m2)/vd3);
		
		return Vi;
	}
	
	public static Vector3d repulsiveVector(SphereNode node, NeuralEdge edge) {
		Vector3d Vi = new Vector3d();
		Vector3d Vm1 = new Vector3d(node.getPosition());
		Vector3d Vm2 = new Vector3d(edge.getMidPoint());
		
		Vector3d Vd = new Vector3d();
		Vd.sub(Vm1, Vm2);
		
		double m1 = node.getMass(FORCE_REPULSIVE | FORCE_WITH_EDGE);;
		double m2 = 1;
		
	
		double vd3 = Math.pow(Vd.length(), 3);
		
		Vi = new Vector3d(Vd);
		Vi.scale((G*m1*m2)/vd3);
		
		return Vi;
	}

	
	/**
	 * iki obje biribine ne kadar uzaksa birbilerini o kadar çok çekerler
	 * @param node
	 * @param repulser
	 * @return
	 */
	public static Vector3d longingVector(SphereNode node, SphereNode wistfulNode) {
		Vector3d Vi;
		Vector3d Vm1 = new Vector3d(node.getPosition());
		Vector3d Vm2 = new Vector3d(wistfulNode.getPosition());

	
		
		Vector3d Vd = new Vector3d();
		Vd.sub(Vm1, Vm2);

		double m1 = node.getMass(FORCE_LONGING);
		double m2 = wistfulNode.getMass(FORCE_LONGING);
		
		
		Vi = new Vector3d(Vd);
		Vi.scale(Vd.length() * (-G*m1*m2));
		
		return Vi;
	}
	

	/**
	 * Bir node ile kosenin orta noktasý biribine ne kadar uzaksa birbilerini o kadar çok çekerler
	 * @param node
	 * @param repulser
	 * @return
	 */
	public static Vector3d longingVector(SphereNode node, NeuralEdge wistfulEdge) {
		Vector3d Vi;
		Vector3d Vm1 = new Vector3d(node.getPosition());
		Vector3d Vm2 = new Vector3d(wistfulEdge.getMidPoint());

	
		
		Vector3d Vd = new Vector3d();
		Vd.sub(Vm1, Vm2);

		double m1 = node.getMass(FORCE_LONGING | FORCE_WITH_EDGE);
		double m2 = 1;
		
		

		
		Vi = new Vector3d(Vd);
		Vi.scale(Vd.length() * (-G*m1*m2));
		
		return Vi;
	}
}
