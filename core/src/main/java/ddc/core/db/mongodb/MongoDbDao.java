package ddc.core.db.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDbDao implements AutoCloseable {
	private MongoDbConfig config = null;
	private MongoClient client = null;

	public MongoDbDao(MongoDbConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void close() throws Exception {
		if (client!=null) {
			client.close();
			client = null;
		}
	}
	
	private MongoClient getClient() {
		if (client != null)
			return client;

		MongoCredential credential = null;
		if (config.hasCredential()) {
			credential = MongoCredential.createCredential(config.getUsername(), config.getDatabase(), config.getPassword().toCharArray());
		}
		if (config.getReplicasetList().size() == 1) {
			String serverAddress = config.getReplicasetList().get(0);
			if (credential!=null) {
				client = new MongoClient(new ServerAddress(serverAddress), Arrays.asList(credential));
			} else {
				client = new MongoClient(new ServerAddress(serverAddress));
			}			
		} else {
			List<ServerAddress> replicaSetAddress = new ArrayList<ServerAddress>();
			config.getReplicasetList().forEach((String s) -> {
				String toks[] = s.split(":");
				if (toks.length != 2)
					throw new RuntimeException("MongoClient - Host Address sintax error, mandatory format is <host>:<port>");
				replicaSetAddress.add(new ServerAddress(toks[0], Integer.valueOf(toks[1])));
			});
			if (credential!=null) {
				client = new MongoClient(replicaSetAddress, Arrays.asList(credential));
			} else {
				client = new MongoClient(replicaSetAddress);
			}	
		}		
		return client;
	}

	public List<Document> find(String collection, String jsonFilter) throws Exception {
		return find(collection, Document.parse(jsonFilter));
	}

	public List<Document> find(String collection, Bson filter) throws Exception {
		MongoClient client = null;
		client = getClient();
		MongoDatabase database = client.getDatabase(config.getDatabase());
		MongoCollection<Document> coll = database.getCollection(collection);
		FindIterable<Document> docs = coll.find(filter);
		List<Document> list = new LinkedList<Document>();
		docs.forEach((Document d) -> {
			list.add(d);
		});
		return list;
	}


//	public Document replace(String collection, String jsonFilter, String jsonDoc) throws MongoException {
//		Bson filter = Document.parse(jsonFilter);
//		Document doc = Document.parse(jsonDoc);
//		return replace(collection, filter, doc);
//	}
//
//	public Document replace(String collection, Bson filter, Document doc) throws MongoException {
//		try (MongoClient client = getClient()) {
//			MongoDatabase database = client.getDatabase(config.getDatabase());
//			MongoCollection<Document> coll = database.getCollection(collection);
//			Document newDoc = coll.findOneAndReplace(filter, doc);
//			if (newDoc == null) {
//				coll.insertOne(newDoc);
//			}
//			return newDoc;
//		}
//	}
}
