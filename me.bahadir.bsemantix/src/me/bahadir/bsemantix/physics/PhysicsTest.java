package me.bahadir.bsemantix.physics;

import static org.junit.Assert.*;

import javax.vecmath.Vector3d;

import me.bahadir.bsemantix.ngraph.SphereNode;

import org.junit.Before;
import org.junit.Test;

public class PhysicsTest {

	@Before
	public void setUp() throws Exception {
	
	}

	@Test
	public void testRepulsiveVector() {
		Physics physics = new Physics();
		
		SphereNode m1 = new SphereNode(new Vector3d(0,4,0));
		SphereNode m2 = new SphereNode(new Vector3d(3,0,0));
//		Vector3d p1 = m1.getPosition();
//		Vector3d p2 = m2.getPosition();
//		Vector3d r = new Vector3d();
//		r.sub(p1,p2);
		Vector3d r = physics.repulsiveVector(m1, m2);
		System.out.println(r.toString());
		System.out.println(r.length());
		
		
	}

	@Test
	public void testResultantVectorLine() {
		fail("Not yet implemented");
	}

}
