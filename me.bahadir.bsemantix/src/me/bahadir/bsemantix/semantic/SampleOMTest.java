package me.bahadir.bsemantix.semantic;

import static org.junit.Assert.*;

import java.io.StringWriter;

import me.bahadir.bsemantix.S;

import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.arp.ExtendedHandler;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public class SampleOMTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetOntologyModel() {
		OntModel model = SampleOM.getOntologyModel();

		

	}

}
