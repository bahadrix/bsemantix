package me.bahadir.bsemantix.parts;


public class TreePair {
	public final boolean isPairArray; 
	public final String name;
	public final String value;
	public final TreePair[] pairArray;
	
	public TreePair(String name, TreePair[] pairs) {
		this.isPairArray = true;
		this.name = name;
		this.value = null;
		this.pairArray = pairs;
	}
	
	public TreePair(String name, String value) {
		this.isPairArray = false;
		this.name = name;
		this.value = value;
		this.pairArray = null;
	}
	
	public void onDoubleClicked() {};
	
}
