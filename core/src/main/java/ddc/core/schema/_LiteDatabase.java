package ddc.core.schema;

import java.util.ArrayList;

public class _LiteDatabase extends ArrayList<_LiteTable> {
	private static final long serialVersionUID = 3718206570101889673L;
	private String database;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	

}
