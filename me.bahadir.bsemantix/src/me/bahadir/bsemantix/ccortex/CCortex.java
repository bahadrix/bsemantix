package me.bahadir.bsemantix.ccortex;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import me.bahadir.bsemantix.ngraph.dtree.DecisionTree;
import me.bahadir.bsemantix.ngraph.dtree.DecisionTree.DecisionTreeData;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


/**
 * MongoDB implementation of ICCOrtex
 * @author Bahadir Katipoglu
 *
 */
public class CCortex{


	
	protected static Logger log = Logger.getLogger(CCortex.class.getSimpleName());
	private final String host, port;
	private MongoClient mongoClient;
	private DB db;

	public static class DectreeDocument extends BasicDBObject {
		public static final String FIELD_SUBJECT_URI = "subjectUri";
		public static final String FIELD_PREDICATE_URI = "predicateUri";
		public static final String FIELD_OBJECT_URI = "objecturi";
		public static final String FIELD_LEAF_URIS = "leafUris";
		public static final String FIELD_XML_DATA = "data";
		
		private String subjectUri;
		private String predicateUri;
		private String objectUri;
		private String xmlData;
		
		private static final long serialVersionUID = 2952853059518958196L;
		
		private DecisionTree.DecisionTreeData data;
		
		private DectreeDocument() {};
		
		public DectreeDocument(DecisionTree decTree) {
			this.data = decTree.getDecisionTreeData();
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				decTree.saveXML(baos);
				String xmlData = new String(baos.toByteArray(), "UTF-8");
	
				this
					.append(FIELD_SUBJECT_URI, data.getSourceUri())
					.append(FIELD_PREDICATE_URI, data.getPredicateUri())
					.append(FIELD_OBJECT_URI, data.getObjectUri())
					.append(FIELD_LEAF_URIS, data.getLeafUris())
					.append(FIELD_XML_DATA, xmlData);
				
				this.subjectUri = data.getSourceUri();
				this.predicateUri = data.getPredicateUri();
				this.objectUri =  data.getObjectUri();
				this.xmlData = xmlData;
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String getSubjectUri() {
			return subjectUri;
		}

		public String getPredicateUri() {
			return predicateUri;
		}


		public String getObjectUri() {
			return objectUri;
		}


		public String getXmlData() {
			return xmlData;
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
	

	/**
	 * Overwrite if the triple already exists
	 * @param doc
	 */
	public void store(DectreeDocument doc) {
		DBCollection coll = db.getCollection("dectrees");
		BasicDBObject query = new BasicDBObject()
			.append("subjectUri",doc.subjectUri)
			.append("predicateUri", doc.predicateUri)
			.append("objecturi", doc.objectUri);
		
		DBCursor cursor = coll.find(query);
		
		if(cursor.hasNext()) { // updating
			log.info("Updating decision system into C-Cortex..");
			coll.update(query, doc);
		} else {
			log.info("Stroring new decision system into C-Cortex..");
			coll.insert(doc);
		
		}
		
		log.info("Store completed!");
	}
	

	public DecisionTreeData load(String subjectUri, String predicateUri, String objectUri) {
		BasicDBObject query = new BasicDBObject("subjectUri",subjectUri)
				.append("predicateUri", predicateUri)
				.append("objecturi", objectUri);
		DBCollection coll = db.getCollection("dectrees");
		DBCursor cursor = coll.find(query);
		
		

		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			String xmlData = (String) obj.get(DectreeDocument.FIELD_XML_DATA);
			//log.info(xmlData);
			return DecisionTreeData.createFromXML(xmlData);


		}

		return null;
		
	}
	
	
}
