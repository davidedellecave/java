package ddc.core.db.berkleydb;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;

public class Test {

	public static void main(String[] args) throws DatabaseException {
		Test t = new Test();
		BerkeleyDb db = t.openDb();
		t.insert(db);
		t.read(db);
	}

	private BerkeleyDb openDb() throws DatabaseException {
		String envPath = "/Users/dellecave/data/berkleydb-store";
		String entityStore = "estore1";

		BerkeleyDb db = new BerkeleyDb();
		db.setup(new File(envPath), entityStore, false);

		return db;

		// EntityStore store = db.getStore();
		// PrimaryIndex<String,Entity1> primaryIdx =
		// store.getPrimaryIndex(String.class, Entity1.class);
		// primaryIdx.put(new Entity1());

	}

	private void insert(BerkeleyDb db) throws DatabaseException {
		 Entity1 e1 = new Entity1();
		 e1.setId("1");
		 e1.setName("davide1");
		 db.putPk(e1);

		 e1.setId("2");
		 e1.setName("davide2");
		 db.putPk(e1);

		 e1.setId("3");
		 e1.setName("davide3");
		 db.putPk(e1);

		Entity1 e = (Entity1) db.getPk(Entity1.class, "1");
		print(e);
	}

	private void read(BerkeleyDb db) throws DatabaseException {
		System.out.println("Multiple objects.....");
		PrimaryIndex<String, Entity1> pi = db.getStore().getPrimaryIndex(String.class, Entity1.class);
		EntityCursor<Entity1> pi_cursor = pi.entities();
		try {
			for (Entity1 e : pi_cursor) {
				print(e);
			}
			// Always make sure the cursor is closed when we are done with it.
		} finally {
			pi_cursor.close();
		}
	}

	private void print(Entity1 e) {
		System.out.print("id:" + e.getId());
		System.out.println(" name:" + e.getName());
	}
}
