package ddc.core.db.berkleydb;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;


public class BerkeleyDb {
	private Environment env = null;
	private EntityStore store = null;

	public void setup(File envHome, String storename, boolean readOnly) throws DatabaseException {
		if (env!=null) return;
				
		// Instantiate an environment configuration object
		EnvironmentConfig envConfig = new EnvironmentConfig();
		// Configure the environment for the read-only state as identified
		// by the readOnly parameter on this method call.
		envConfig.setReadOnly(readOnly);
		// If the environment is opened for write, then we want to be
		// able to create the environment if it does not exist.
		envConfig.setAllowCreate(!readOnly);
		// Instantiate the Environment. This opens it and also possibly
		// creates it.
		env = new Environment(envHome, envConfig);
	    
	    StoreConfig storeConfig = new StoreConfig();	    
	    storeConfig.setAllowCreate(!readOnly);
	    // Open the environment and entity store
	    store = new EntityStore(env, storename, storeConfig);
	    
	    setupShutdownHook(this);
		
	}

	private void setupShutdownHook(final BerkeleyDb db) {
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        try {
					db.close();
					mainThread.join();
				} catch (DatabaseException | InterruptedException e) {
					throw new RuntimeException(e);
				}
		    }
		});
	}
	
	public Environment getEnv() {
		return env;
	}
	
	public EntityStore getStore() {
		return store;
	}

	// Close the environment
	public void close() throws DatabaseException {
		if (store != null) {
			System.out.println("Closing entity store...");
			store.close();
		}
		if (env != null) {
			System.out.println("Closing db...");
			env.close();
		}
	}
	
	public void putPk(Object o) throws DatabaseException {
		@SuppressWarnings("unchecked")
		PrimaryIndex<String, Object> primaryIdx = (PrimaryIndex<String, Object>) store.getPrimaryIndex(String.class, o.getClass());
		primaryIdx.put(o);
		
	}

	public Object getPk(Class clazz, String key) throws DatabaseException {
		@SuppressWarnings("unchecked")
		PrimaryIndex<String, Object> primaryIdx = (PrimaryIndex<String, Object>) store.getPrimaryIndex(String.class, clazz);
		return primaryIdx.get(key);
	}

}
