package me.bahadir.bsemantix.synaptix;

public class Leaf extends Node {
	
	public static final Leaf PASS = new Leaf(true);
	public static final Leaf NOT_PASS = new Leaf(false);
	
	private boolean pass;

	
	
	private Leaf(boolean pass) {
		this.pass = pass;
	}



	public boolean getClearance() {
		return pass;
	}


	
}