package me.bahadir.bsemantix.semantic;

import java.util.logging.Logger;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.ngraph.NeuralEdge;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.ngraph.Restrict;
import me.bahadir.bsemantix.ngraph.RestrictedEdge;
import me.bahadir.bsemantix.ngraph.SphereNode;
import me.bahadir.bsemantix.ngraph.SphereNode.ResourceType;
import me.bahadir.bsemantix.ngraph.SynapticEdge;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OntoAdapter {
	public static final int GET_PROPERTIES_FROM_EQUIVALENT = 2;
	public static final int LOAD_INDIVIDUALS = 4;

	private static int SETTINGS = 0;

	protected static Logger log = Logger.getLogger(OntoAdapter.class.getSimpleName());

	private OntModel model;

	public OntoAdapter(OntModel model) {

		SETTINGS |= LOAD_INDIVIDUALS;
		log.info("Ontology adapter settings bitmask: " + Integer.toBinaryString(SETTINGS));
		loadModel(model);
	}

	public void loadModel(OntModel model) {

		this.model = model;

	}

	

	public NeuralGraph createNeuralGraph() {

		NeuralGraph ng = new NeuralGraph();


		log.info("Adding classes..");
		ExtendedIterator<OntClass> itClass;
		itClass = model.listClasses();
		while (itClass.hasNext()) {

			OntClass cls = itClass.next();

			if (!cls.isRestriction() && cls.getURI() != null) {
				System.out.println("Adding class " + cls.getLocalName());

				ng.addVertex(new SphereNode(cls.getLocalName(), S.randomVector(10), cls));

			}
			
		}

		
		if(S.bitTest(SETTINGS, LOAD_INDIVIDUALS)) {
			log.info("Adding individuals..");
			ExtendedIterator<Individual> inds = model.listIndividuals();
			
			while(inds.hasNext()) {
				Individual ind = inds.next();
				ng.addVertex(new SphereNode(ind.getLocalName(), S.randomVector(10), ind));
			}
		}
		
		
		log.info("Connecting nodes..");
		for (SphereNode source : ng.vertexSet()) {

			if (source.resourceType == ResourceType.CLASS) {
				
				OntClass cls = source.getOntClass();
				StmtIterator sit = cls.listProperties();

				// add direct edges
				while (sit.hasNext()) {
					Statement st = sit.next();

					if (st.getObject().isResource()) {
						Resource object = st.getObject().asResource();

						SphereNode target = ng.getNodeByUri(object.getURI());
						if (target != null && !target.equals(source)) {
							addEdge(ng, source, target, st.getPredicate());
						}

					}

				}

				// Equivalent and restr edges
				ExtendedIterator<OntClass> equit = cls.listEquivalentClasses();
				while (equit.hasNext()) {
					OntClass eq = equit.next();

					if (eq.isRestriction()) {
						Restriction restr = eq.asRestriction();

						try {
							addRestriction(ng, cls.getURI(), restr);
						} catch (Exception e) {
							log.info(e.getMessage());
							e.printStackTrace();
						}
					} else {
						if(S.bitTest(SETTINGS, GET_PROPERTIES_FROM_EQUIVALENT)) {
							throw new UnsupportedOperationException("equivalent class adaptation (except by restriction) not implemented yet.");
						}
					}

				}

				
				
			} else if (source.resourceType == ResourceType.INDIVIDUAL) {
				Individual ind = source.getIndividual();
				StmtIterator sit = ind.listProperties();

				// add direct edges
				while (sit.hasNext()) {
					Statement st = sit.next();

					if (st.getObject().isResource()) {
						Resource object = st.getObject().asResource();

						SphereNode target = ng.getNodeByUri(object.getURI());
						if (target != null && !target.equals(source)) {
							addEdge(ng, source, target, st.getPredicate());
						}

					}

				}
				
			}

		}

		

		return ng;
	}

	private void addRestriction(NeuralGraph ng, String sourceUri, Restriction rest) throws Exception {
		Property pIsSynaptic = model.createProperty("http://bahadir.me/synapse#isSynaptic");
		SphereNode source = ng.getNodeByUri(sourceUri);
		if (source == null)
			throw new Exception("Source node not found with uri " + sourceUri);
		if (source.resourceType != ResourceType.CLASS)
			throw new Exception("Source must be a ontclass " + sourceUri);
		OntClass cls = source.getOntClass();
		Restrict restrict = Restrict.createFromOwlRestriction(cls, rest);
		if (restrict != null) {
			if (rest.getOnProperty().hasProperty(pIsSynaptic)) {

				ng.addEdge(new SynapticEdge(ng, restrict));

			} else {

				ng.addEdge(new RestrictedEdge(ng, restrict));

			}
		}

	}

	private void addEdge(NeuralGraph ng, SphereNode source, SphereNode target, Property p) {

		NeuralEdge edge = new NeuralEdge(source, target);

		edge.setProperty(p);
		edge.setName(p.getLocalName());
		ng.addEdge(edge);
	}

	/**
	 * If prefix is null returns true else test ns
	 * 
	 * @param r
	 * @param prefix
	 * @return
	 */
	public static boolean isResourceInNS(Resource r, String prefix) {
		if (prefix == null)
			return true;
		return r.getNameSpace().equals(prefix);
	}

	public OntModel getModel() {
		return model;
	}

	public void setModel(OntModel model) {
		this.model = model;
	}

}
