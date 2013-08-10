 
package me.bahadir.bsemantix.handlers;

import java.io.File;
import java.util.logging.Logger;

import me.bahadir.bsemantix.S;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;

public class ConnectTDB {
	
	public static final String TOPIC_CONNECTED = "TDB_CONNECTED";
	protected static Logger log = Logger.getLogger(ConnectTDB.class.getSimpleName());
	
	@Execute
	public void execute() {
		String path = Platform.getInstanceLocation().getURL().getPath() + "tdbstore/default";

		
		File file = new File(path);
		if(!file.exists()) { // yoksa oluþtur
			file.mkdirs();
		}
        if (file.isDirectory()) { //varsa ve klasörse
        	Dataset dataset;
			if (file.list().length == 0) { // içinde hiç dosya yoksa
                dataset = TDBFactory.createDataset(path);
                log.info("creating empty model in " + path);
                dataset.begin(ReadWrite.WRITE);
                dataset.getDefaultModel().add(createNewOntModel());
                dataset.commit();
                System.out.println(" (TAMAM)");
            } else {
            	log.info("loading empty model from " + path);
                dataset = TDBFactory.createDataset(path);
            }
	        S.currentDataSet = TDBFactory.createDataset();
	        
	        S.broker.send(TOPIC_CONNECTED, S.currentDataSet);
        } else {
        	System.out.println(path + " is not a directory!");
        }
	}
		
	
	private OntModel createNewOntModel() {
		OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
		om.setNsPrefix("bsx", S.NS_BSX);
		return om;
	}
}