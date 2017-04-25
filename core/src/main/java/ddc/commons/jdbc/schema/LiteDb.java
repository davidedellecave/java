package ddc.commons.jdbc.schema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LiteDb {
	// TABLE_TYPE String => table type. Typical types are "TABLE",
	// "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	// "SYNONYM"
	private final static String[] TABLE_TYPES = { "TABLE", "VIEW" };
	private String databaseName = "";
	private List<LiteDbCatalog> catalogs = new ArrayList<LiteDbCatalog>();

	public static LiteDb build(String databaseName, Connection connection) throws SQLException {
		LiteDb liteDb = new LiteDb();
		liteDb.setDatabaseName(databaseName);
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rsCatalogs = meta.getCatalogs();
		while (rsCatalogs.next()) {
			String catalogName = rsCatalogs.getString("TABLE_CAT");
			LiteDbCatalog liteCatalog = new LiteDbCatalog(catalogName, liteDb);
			liteDb.getCatalogs().add(liteCatalog);
			//
			ResultSet rsSchema = meta.getSchemas();
			while (rsSchema.next()) {
				String schemaName = rsSchema.getString("TABLE_SCHEM");
				LiteDbSchema liteSchema = new LiteDbSchema(schemaName, liteCatalog);
				liteCatalog.getSchemas().add(liteSchema);
				//
				ResultSet rsTable = meta.getTables(catalogName, schemaName, null, TABLE_TYPES);
				while (rsTable.next()) {
					String tableName = rsTable.getString("TABLE_NAME");
					String tableType = rsTable.getString("TABLE_TYPE");
					LiteDbTable liteTable = new LiteDbTable(tableName, liteSchema);
					liteTable.setTableType(tableType);
					liteSchema.getTables().add(liteTable);
					//
					ResultSet rsColumn = meta.getColumns(catalogName, schemaName, tableName, null);
					int colCounter = 0;
					while (rsColumn.next()) {
						colCounter++;
						String columnName = rsColumn.getString("COLUMN_NAME");
						int dataType = rsColumn.getShort("DATA_TYPE");
						JDBCType jdbcType = JDBCType.valueOf(dataType);
						boolean isNullable = rsColumn.getInt("NULLABLE") == 1;
						int columnSize = rsColumn.getInt("COLUMN_SIZE");
						LiteDbColumn c = new LiteDbColumn(columnName, jdbcType, isNullable, columnSize, colCounter);
						liteTable.getColumns().add(c);
					}
				}
			}
		}
		return liteDb;
	}

	public List<LiteDbTable> findByCol(String colName) {
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				for (LiteDbTable t : s.getTables()) {
					for (LiteDbColumn cl : t.getColumns()) {
						if (cl.getName().contains(colName)) {
							list.add(t);
							break;
						}
					}
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> findByTable(String tableName) {
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				for (LiteDbTable t : s.getTables()) {
					if (t.getTableName().contains(tableName))
						list.add(t);
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> findBySchema(String schemaName) {
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				if (s.getSchemaName().contains(schemaName)) {
					for (LiteDbTable t : s.getTables()) {
						list.add(t);
					}
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> findByCatalog(String catalogName) {
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			if (c.getCatalogName().contains(catalogName)) {
				for (LiteDbSchema s : c.getSchemas()) {
					for (LiteDbTable t : s.getTables()) {
						list.add(t);
					}
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> getAllTables() {
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				for (LiteDbTable t : s.getTables()) {
					list.add(t);
				}
			}
		}
		return list;
	}
	
	public List<LiteDbSchema> getAllSchemas() {
		List<LiteDbSchema> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				list.add(s);
			}
		}
		return list;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public List<LiteDbCatalog> getCatalogs() {
		return catalogs;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Database:[" + databaseName + "] ");
		b.append("\n");
		for (LiteDbCatalog c : catalogs) {
			b.append(c.toString());
		}
		return b.toString();
	}

}
