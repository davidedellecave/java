package ddc.commons.jdbc.schema;

import java.util.ArrayList;
import java.util.List;

public class LiteDbSchema {
	private String schemaName="";
	private LiteDbCatalog catalog = new LiteDbCatalog();
	private List<LiteDbTable> tables = new ArrayList<LiteDbTable>();
	
	public LiteDbSchema() {
	}	
	
	public LiteDbSchema(String schemaName, LiteDbCatalog catalog) {
		this.schemaName=schemaName;
		this.catalog=catalog;
	}	
	
	public LiteDbCatalog getCatalog() {
		return catalog;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public List<LiteDbTable> getTables() {
		return tables;
	}
	public void setTables(List<LiteDbTable> tables) {
		this.tables = tables;
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Database:[" + catalog.getDatabase().getDatabaseName() + "] ");
		b.append("Catalog:[" + catalog.getCatalogName() + "] ");
		b.append("Schema:[" + schemaName + "] ");
		b.append("\n");
		for (LiteDbTable t : tables) {
			b.append(t.toString());
			b.append("\n");
		}
		return b.toString();
	}
}
