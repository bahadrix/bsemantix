package me.bahadir.bsemantix.ngraph.dtree;

import java.util.LinkedHashSet;


public class SynonymSet extends LinkedHashSet<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3171096688379661478L;

	@Override
	public boolean add(String e) {
		if(e.contains(",")) {
			
			System.out.println("Synonyms can't contain comma.");
			return false;
		}
			
		return super.add(e);
	}



	
	
	
}
