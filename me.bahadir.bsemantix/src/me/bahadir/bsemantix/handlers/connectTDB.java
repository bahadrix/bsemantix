 
package me.bahadir.bsemantix.handlers;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;

import com.hp.hpl.jena.ontology.OntModel;

public class connectTDB {
	

	
	@Execute
	public void execute() {
//		  File file = new File(Platform.getWS() + "/store/db");
//	        if (file.isDirectory()) {
//	            if (file.list().length == 0) {
//	                dataset = TDBFactory.createDataset("store/db");
//	                System.out.print("Ilk kullanim icin TDB veri tabani olusturuluyor, lutfen bekleyiniz..");
//	                dataset.begin(ReadWrite.WRITE);
//	                dataset.getDefaultModel().add(getOntologyModel());
//	                dataset.commit();
//	                System.out.println(" (TAMAM)");
//	            } else {
//	                dataset = TDBFactory.createDataset("store/db");
//	            }
//	        }
	}
		
}