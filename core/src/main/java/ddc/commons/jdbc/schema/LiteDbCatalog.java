package ddc.commons.jdbc.schema;

import java.util.ArrayList;
import java.util.List;

public class LiteDbCatalog {
	private String catalogName = "";
	private LiteDb database = new LiteDb();
	private List<LiteDbSchema> schemas = new ArrayList<LiteDbSchema>();

	public LiteDbCatalog() {}
	
	public LiteDbCatalog(String catalogName, LiteDb database) {
		this.catalogName=catalogName;
		this.database=database;
	}

	public LiteDb getDatabase() {
		return database;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public List<LiteDbSchema> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<LiteDbSchema> schemas) {
		this.schemas = schemas;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Database:[" + database.getDatabaseName() + "] ");
		b.append("Catalog:[" + catalogName + "] ");
		b.append("\n");
		for (LiteDbSchema ts : schemas) {
			b.append(ts.toString());
			b.append("\n");
		}
		return b.toString();
	}

}
