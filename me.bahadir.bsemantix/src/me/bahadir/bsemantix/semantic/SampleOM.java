package me.bahadir.bsemantix.semantic;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SampleOM {
	public static final String NS_BAHA = "http://bahadir.me/semantic/animals#";
	
	 public static OntModel getOntologyModel() {
	        
	        // Yeni bir model olustur
	        //OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //inference yapar bu
	        OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF); //inference yapar bu
	        
	        om.setNsPrefix("bx", NS_BAHA); // Ozel namespace'i ekle
	        
	        // Bu ozellik true olduðunda, ozelliðin sinif hayvan turu olarak secilecektir.
	        OntProperty species = om.createOntProperty("bx:Species");
	        
	        
	        //hayvanlar alemi
	        OntClass animal = om.createClass("bx:Animal");

	        // Yetenekler
	        OntClass ability = om.createClass("bx:Ability");
	        
	        OntClass flying = om.createClass("bx:Flying");
	        OntClass swimming = om.createClass("bx:Swimming");
	        swimming.setSuperClass(ability);
	        flying.setSuperClass(ability);
	        
	        

	        // Beslenme sekli
	        OntClass carnivore = om.createClass("bx:Carnivore"); //etcil
	        OntClass herbivore = om.createClass("bx:Herbivore"); //otcul
	        
	
	        
	        // Av->avci iliskisi
	        OntProperty hunt = om.createOntProperty("bx:hunt"); 
	        
	        

	        // Yeteneði var ozelliði
	        OntProperty hasAbility = om.createOntProperty("bx:hasAbility");

	        // Beslenme sekli ozelliði
	        OntProperty hasDiet = om.createOntProperty("bx:hasDiet");

	        // Kuslar sinifi
	        OntClass bird = om.createClass("bx:Bird");
	        bird.setSuperClass(animal);
	        bird.setEquivalentClass(om.createHasValueRestriction(null, hasAbility, flying)); // Kuslar ucarlar

	        // Bocekler sinifi
	        OntClass insect = om.createClass("bx:Insect");
	        insect.setSuperClass(animal);
	        
	        // Surungenler sinifi
	        OntClass reptile = om.createClass("bx:Reptile");
	        reptile.setSuperClass(animal);

	        // Timsah turu
	        OntClass croc = om.createClass("bx:Timsah");
	        croc.setPropertyValue(species, om.createTypedLiteral(true));
	        croc.setEquivalentClass(
	                om.createIntersectionClass(null, om.createList(
	                new RDFNode[]{
	                    reptile,
	                    om.createHasValueRestriction(null, hasAbility, swimming), // yuzebilir
	                    om.createHasValueRestriction(null, hasDiet, carnivore), // etcildir
	                    om.createHasValueRestriction(null, hunt, bird) // kuslari avlayabilir
	                })));


	        // Karga turu
	        OntClass crow = om.createClass("bx:Karga");
	        crow.setPropertyValue(species, om.createTypedLiteral(true));
	        crow.setEquivalentClass(
	                om.createIntersectionClass(null, om.createList(
	                new RDFNode[]{ // Karga hem et hem bitki yer
	                    bird,
	                    om.createHasValueRestriction(null, hasDiet, herbivore),
	                    om.createHasValueRestriction(null, hasDiet, carnivore),
	                    om.createHasValueRestriction(null, hunt, insect)
	                })));


	        // Kelebek turu
	        OntClass butterfly = om.createClass("bx:Kelebek");
	        butterfly.setPropertyValue(species, om.createTypedLiteral(true));
	        butterfly.setEquivalentClass(
	                om.createIntersectionClass(null, om.createList(
	                new RDFNode[]{ // Karga hem et hem bitki yer
	                    insect,
	                    om.createHasValueRestriction(null, hasDiet, herbivore),
	                    om.createHasValueRestriction(null, hasAbility, flying)
	                })));

	        // Bukalemun turu
	        OntClass chameleon = om.createClass("bx:Bukalemun");
	        chameleon.setPropertyValue(species, om.createTypedLiteral(true));
	        chameleon.setEquivalentClass(
	                om.createIntersectionClass(null, om.createList(
	                new RDFNode[]{ // Karga hem et hem bitki yer
	                    reptile,
	                    om.createHasValueRestriction(null, hasDiet, herbivore),
	                    om.createHasValueRestriction(null, hasDiet, carnivore),
	                    om.createHasValueRestriction(null, hunt, insect)
	                    
	                })));
	        
	        // Ornek bireyleri olustur
	        om.createIndividual("bx:timsahmet", croc);
	        om.createIndividual("bx:timsahu", croc);
	        om.createIndividual("bx:kargaffur", crow);
	        om.createIndividual("bx:kargamze", crow);
	        om.createIndividual("bx:kelebekir", butterfly);
	        om.createIndividual("bx:kelebekrem", butterfly);
	        om.createIndividual("bx:bukalemunise", chameleon);
	        om.createIndividual("bx:bukalemunalan", chameleon);

//	        for(int i = 0; i < 100; i++) {
//	        	om.createIndividual("bx:kbmusa_" + String.valueOf(i), butterfly);
//	        	om.createIndividual("bx:bukalemunise" + String.valueOf(i), chameleon);
//	        	
//	        }
	        
		return om;

	}
}
