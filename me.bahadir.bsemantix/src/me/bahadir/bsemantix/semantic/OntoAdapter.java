package me.bahadir.bsemantix.semantic;

import java.util.ArrayList;
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
import me.bahadir.bsemantix.ngraph.SynapticEdge;
import me.bahadir.bsemantix.ngraph.Synapse;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.CardinalityQRestriction;
import com.hp.hpl.jena.ontology.CardinalityRestriction;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.QualifiedRestriction;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.ontology.impl.CardinalityQRestrictionImpl;
import com.hp.hpl.jena.ontology.impl.CardinalityRestrictionImpl;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.InvalidPropertyURIException;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public class OntoAdapter {
	protected static Logger log = Logger.getLogger(OntoAdapter.class.getSimpleName());
	
	private OntModel model;
	private List<SynapticProperty> synapticProperties;
	private List<Synapse> synapses;

	public OntoAdapter(OntModel model) {
		loadModel(model);
	}
	
	public void loadModel(OntModel model) {
		log.info("Creating sample ontology model..");
		this.model = model;
		
		loadSynapticProperties();
	}

	private void loadSynapticProperties() {
		log.info("Searching synaptic properties:");
		synapticProperties = new ArrayList<SynapticProperty>();
		
		Property pIsSynaptic = model.createProperty("http://bahadir.me/synapse#isSynaptic");
		Iterator<Resource> it = model.listResourcesWithProperty(pIsSynaptic);
		
		while(it.hasNext()) {
			Resource r = it.next();
			try {
				Property p = model.getProperty(r.getURI());
				synapticProperties.add(new SynapticProperty(p));
				log.info("\t" + p.getURI());
			} catch (UnsupportedOperationException e) {
				log.warning(r.getLocalName() + " has error: " + e.getLocalizedMessage()); 
			} catch (InvalidPropertyURIException e ) {
				//Skip if resource is not property
			}
		}
		
		resolveSynapses();
		
	}
	
	public void resolveSynapses() {
		log.info("Resolving synapses..");
		synapses = new ArrayList<>();
		Iterator<OntClass> it = model.listClasses();
		
		
		while(it.hasNext()) { // butun tanimlanmis siniflari gez
			OntClass cls = it.next();
			Iterator<OntClass> eqit = cls.listEquivalentClasses();
			
			while(eqit.hasNext()) { // bu siniflarin butun equivalent tanimlamalarini gez.
				OntClass eqCls = eqit.next();
				if(eqCls.isRestriction()) { // bu equ.cls. bir restriction ise
					
					Restriction rest = eqCls.asRestriction();
					
					
					
					//Synaptic property ile mi ilgili ona bak
					for(SynapticProperty sp :synapticProperties) {
						

						
						
						
						if(rest.getOnProperty().equals(sp.getOwlProperty())) {
							//bizim synaptic proplardan biri ile ilgili bir rest. içeriyor.
							//Ozaman bi sinaps oluþtur
							
							Synapse synapse = new Synapse(sp);
							
							synapse.setSource(cls);
							
							log.info("\t" + cls.getLocalName() + ":" + sp.getOwlProperty().getLocalName());
							OntClass range = null;
							
							//CARDINALITY 0-*
							if(rest.isSomeValuesFromRestriction()) { // some values restriction
								synapse.setMinCardinality(0);								
								log.info("\t\t(some)");
								SomeValuesFromRestriction someRest = rest.asSomeValuesFromRestriction();
								range = someRest.getSomeValuesFrom().as(OntClass.class);
								
							} else if(rest.isAllValuesFromRestriction()){
							// CARDINALITY 0-* 
								synapse.setMinCardinality(0);
								log.info("\t\t(allFrom)");
								AllValuesFromRestriction allRest = rest.asAllValuesFromRestriction();
								range = allRest.getAllValuesFrom().as(OntClass.class);
							} else {
								
								Property pQualifiedCard = model.getProperty("http://www.w3.org/2002/07/owl#qualifiedCardinality");
								Property pMinQualifiedCard = model.getProperty("http://www.w3.org/2002/07/owl#minQualifiedCardinality");
								Property pMaxQualifiedCard = model.getProperty("http://www.w3.org/2002/07/owl#maxQualifiedCardinality");
								Property pOnClass = model.getProperty("http://www.w3.org/2002/07/owl#onClass");
								
								if(rest.hasProperty(pQualifiedCard)) {
									// CARDINALITY n
									
									int cardInt = rest.getPropertyValue(pQualifiedCard).asLiteral().getInt();
									synapse.setExactCardinality(cardInt);
									log.info(String.format("\t\t(exact %d)",cardInt));
									range = rest.getPropertyValue(pOnClass).as(OntClass.class);
								} else if(rest.hasProperty(pMinQualifiedCard)) { // CARDINALITY n-*
									int cardInt = rest.getPropertyValue(pQualifiedCard).asLiteral().getInt();
									range = rest.getPropertyValue(pOnClass).as(OntClass.class);
									synapse.setMinCardinality(cardInt);
								} else if(rest.hasProperty(pMaxQualifiedCard)) { // CARDINALITY *-n
									int cardInt = rest.getPropertyValue(pQualifiedCard).asLiteral().getInt();
									range = rest.getPropertyValue(pOnClass).as(OntClass.class);
									synapse.setMaxCardinality(cardInt);
								} else {
									log.warning(String.format("Restriction format is not supported for property '%s' on class '%s'. Synaptic link skipped.", sp.getOwlProperty().getLocalName(), cls.getLocalName()));
								}
								
								
							}
							
							if(range != null) {
								synapse.setRange(range);
								synapses.add(synapse);
								
								if(range.isUnionClass()) {
									UnionClass uc = range.asUnionClass();
									Iterator<? extends OntClass> itop = uc.listOperands();
									while(itop.hasNext()) {
										OntClass op = itop.next();
										log.info("\t\t" + op.getLocalName());
									}
								} else {
									log.info("\t\t" + range.getLocalName());
								}
							}
							
						}
					}		
				}
			}
		}
		
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
						SphereNode source = resourceMap.get(r);
						SphereNode target = resourceMap.get(object);
				
						if(source == null || target == null) continue;
						
						NeuralEdge edge = new NeuralEdge(source, target);
				
						edge.setProperty(p);
						edge.setName(p.getLocalName());
						ng.addEdge(edge);
						//System.out.println("\t" + p + " " + object.getLocalName());
					}
				}
			}

		}
		
		addSynapticEdges(ng);
		
		return ng;
	}

	private void addSynapticEdges(NeuralGraph ng) {
		
		for(Synapse synapse : synapses) {
			
			try {
				ng.addEdge(new SynapticEdge(ng, synapse));
			} catch (IllegalArgumentException e) {
				log.warning(e.getMessage());
			}
			
//			SphereNode source = ng.getNodeByUri(sp.getSource().getURI());
//			SphereNode target = ng.getNodeByUri(sp.getRange().getURI());
//			
//			if(source != null && target != null) {
//				NeuralEdge edge = new NeuralEdge(source, target);
//				edge.setName("synaptic-edge");
//				ng.addEdge(edge);
//				
//			}
			
			
		}
		
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
