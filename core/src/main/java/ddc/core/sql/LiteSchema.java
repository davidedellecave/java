package ddc.core.sql;

import java.sql.JDBCType;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LiteSchema {
	private String schema;
	private String table;
	private List<LiteColumn> columns = new ArrayList<LiteColumn>();
	
	public static LiteSchema createInstance(String schema, ResultSetMetaData meta) throws SQLException {
		LiteSchema s = new LiteSchema();
		s.setSchema(schema);
		s.setTable(meta.getTableName(1));
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			LiteColumn c = new LiteColumn();
			c.setName(meta.getColumnName(i));
			c.setNullable(meta.isNullable(i)==ResultSetMetaData.columnNullable);
			c.setSize(meta.getPrecision(i));
			c.setType(JDBCType.valueOf(meta.getColumnType(i)));
			s.getColumns().add(c);
		}
		return s;
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public List<LiteColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<LiteColumn> columns) {
		this.columns = columns;
	}
	
	private static Map<JDBCType, String> defaultTypeMap = new TreeMap<>();
	static {
		for (JDBCType t : JDBCType.values()) {
			defaultTypeMap.put(t, t.getName());
		}
	}
	
	public String generateCreateTable() {
		return generateCreateTable(defaultTypeMap);
	}
	public String generateCreateTable(Map<JDBCType, String> typeMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + table + " (");
		for (LiteColumn c : getColumns()) {
			sql.append(c.getName());
			String typeName = typeMap.get(c.getType());
			sql.append(" " + typeName);
			if (!c.isNullable()) {
				sql.append(" IS NOT NULL");	
			}
			sql.append(",");
		}
		sql = sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		return sql.toString();
	}
	
//	public static Map<JDBCType, String> map= new TreeMap<>();
//	static {
//		map.put(JDBCType.ARRAY, "");
//		map.put(JDBCType.BIGINT, "");
//		map.put(JDBCType.BINARY, "");
//		map.put(JDBCType.BIT, "");
//		map.put(JDBCType.BLOB, "");
//		map.put(JDBCType.BOOLEAN, "");
//		map.put(JDBCType.CHAR, "");
//		map.put(JDBCType.CLOB, "");
//		map.put(JDBCType.DATALINK, "");
//		map.put(JDBCType.DATE, "");
//		map.put(JDBCType.DECIMAL, "");
//		map.put(JDBCType.DISTINCT, "");
//		map.put(JDBCType.DOUBLE,  "");
//		map.put(JDBCType.FLOAT,  "");
//		map.put(JDBCType.INTEGER, "");
//		map.put(JDBCType.JAVA_OBJECT,  "");
//		map.put(JDBCType.LONGNVARCHAR,  "");
//		map.put(JDBCType.LONGVARBINARY,  "");
//		map.put(JDBCType.NCHAR,  "");
//		map.put(JDBCType.NCLOB,  "");
//		map.put(JDBCType.NULL,  "");
//		map.put(JDBCType.NUMERIC,  "");
//		map.put(JDBCType.NVARCHAR,  "");
//		map.put(JDBCType.OTHER,  "");
//		map.put(JDBCType.REAL,  "");
//		map.put(JDBCType.REF,  "");
//		map.put(JDBCType.REF_CURSOR, "");
//		map.put(JDBCType.ROWID, "");
//		map.put(JDBCType.SMALLINT,"");
//		map.put(JDBCType.SQLXML, "");
//		map.put(JDBCType.STRUCT, "");
//		map.put(JDBCType.TIME, "");
//		map.put(JDBCType.TIME_WITH_TIMEZONE, "");
//		map.put(JDBCType.TIMESTAMP_WITH_TIMEZONE, "");
//		map.put(JDBCType.TINYINT,"");
//		map.put(JDBCType.VARBINARY, "");
//		map.put(JDBCType.VARCHAR, "");
//	}

}
