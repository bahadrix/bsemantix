package me.bahadir.bsemantix.semantic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.sql.rowset.Predicate;
import javax.vecmath.Vector3d;

import me.bahadir.bsemantix.NeuralBench;
import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.SphereNode;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public class OntoAdapter {
	protected static Logger log = Logger.getLogger(OntoAdapter.class.getSimpleName());
	
	private OntModel model;


	public OntoAdapter() {

		log.info("Creating sample ontology model..");
		this.model = SampleOM.getOntologyModel();
	}

	/**
	 * Creates Neural Graph from all namespaces
	 * @return
	 */
	public NeuralGraph createNeuralGraph() {
		return createNeuralGraph(null);
	}
	
	/**
	 * Create Neural Graph from resources starts with nsPrefix
	 * @param nsPrefix
	 * @return
	 */
	public NeuralGraph createNeuralGraph(final String nsPrefix) {
		NeuralGraph ng = new NeuralGraph();
		ng.setNsPrefix(nsPrefix);
		
		// model.write(System.out, "N3");
		ExtendedIterator<OntClass> itClass = model.listClasses().filterDrop(new Filter<OntClass>() {

			@Override
			public boolean accept(OntClass c) {
				if (c.getNameSpace() == null)
					return true;
				if (!isResourceInNS(c, nsPrefix))
					return true;
				return false;
			}
		});

		
		
		Map<Resource, SphereNode> resourceMap = new LinkedHashMap<>();


		while (itClass.hasNext()) {
			OntClass c = itClass.next();
			
			SphereNode node = new SphereNode(c.getLocalName(), S.randomVector(10) );
			node.setOntClass(c);
			ng.addVertex(node);
			resourceMap.put(c, node);
			
			Iterator<Individual> indIt = model.listIndividuals(c);
			
			while(indIt.hasNext()) {
				
				Individual ind = indIt.next();
				if(resourceMap.containsKey(ind)) continue;
				SphereNode indNode = new SphereNode(ind.getLocalName(), S.randomVector(10), SphereNode.ResourceType.INDIVIDUAL);
				ng.addVertex(indNode);
				resourceMap.put(ind, indNode);
				
			}
		}
		
		for(Resource r : resourceMap.keySet()) {

			
			
			StmtIterator sit = r.listProperties();

			while (sit.hasNext()) {
				Statement s = sit.next();

				if (s.getObject().isResource() && !s.getObject().equals(s.getSubject())) {
					Resource object = s.getObject().asResource();
					if (object.getNameSpace() != null && isResourceInNS(object, nsPrefix)) {
						Property p = s.getPredicate();
						NeuralEdge edge = new NeuralEdge(resourceMap.get(r), resourceMap.get(object));
						edge.setProperty(p);
						edge.setName(p.getLocalName());
						ng.addEdge(edge);
						//System.out.println("\t" + p + " " + object.getLocalName());
					}
				}
			}

		}
		
		
		


		return ng;
	}

	/**
	 * If prefix is null returns true else test ns
	 * @param r
	 * @param prefix
	 * @return
	 */
	public static boolean isResourceInNS(Resource r, String prefix) {
		if(prefix == null) return true;
		return r.getNameSpace().equals(prefix);
	}

	public OntModel getModel() {
		return model;
	}

	public void setModel(OntModel model) {
		this.model = model;
	}


}
