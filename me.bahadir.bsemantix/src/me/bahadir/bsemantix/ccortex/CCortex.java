package me.bahadir.bsemantix.ccortex;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import me.bahadir.bsemantix.ngraph.dtree.DecisionTree;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;



public class CCortex {

	protected static Logger log = Logger.getLogger(CCortex.class.getSimpleName());
	private final String host, port;
	private MongoClient mongoClient;
	private DB db;

	public static class DectreeDocument extends BasicDBObject {


		private static final long serialVersionUID = 2952853059518958196L;

		private final DecisionTree.DecisionTreeData data;
		
		public DectreeDocument(DecisionTree decTree) {
			this.data = decTree.getDecisionTreeData();
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				decTree.saveXML(baos);
				String xmlData = new String(baos.toByteArray(), "UTF-8");
	
				this
					.append("sourceUri", data.getSourceUri())
					.append("predicateUri", data.getPredicateUri())
					.append("leafUris", data.getLeafUris())
					.append("data", xmlData);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	
	
	private static CCortex cCortex = null;
	
	public static CCortex getInstance(String host, String port) {
		if(cCortex != null) {
			if(cCortex.host.equals(host) && cCortex.port.equals(port)) {
				return cCortex;
			}
		}
		return new CCortex(host, port);
	}
	

	
	private CCortex(String host, String port) {
		this.host = host;
		this.port = port;
		try {
			
			mongoClient = new MongoClient(host, Integer.parseInt(port));
			db = mongoClient.getDB("bsxdb");
			log.info("CCortex connected " + host + "on port " + port);
			
			//boolean auth = db.authenticate(myUserName, myPassword);
			
			
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (UnknownHostException e) {
			log.severe("Can't connect to C-Cortex at" + host + "on port " + port);
			e.printStackTrace();
		}
		
	};
	
	public void store(DectreeDocument doc) {
		log.info("Storing decision system into C-Cortex..");
		DBCollection coll = db.getCollection("dectrees");
		coll.insert(doc);
		log.info("Store completed!");
	}
	
	public void getDectreeDocument(String sourceUri, String predicateUri, String leafUri) {
		
	}
}
