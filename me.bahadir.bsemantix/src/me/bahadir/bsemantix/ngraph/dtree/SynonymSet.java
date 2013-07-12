package me.bahadir.bsemantix.ngraph.dtree;

import java.util.LinkedHashSet;

import javax.xml.bind.annotation.XmlRootElement;

import me.bahadir.bsemantix.S;

import com.hp.hpl.jena.sparql.util.StringUtils;


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
