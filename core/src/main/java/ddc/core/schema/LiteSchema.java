package ddc.core.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LiteSchema {
	private String schema;
	private String table;
	private List<LiteColumn> columns = new ArrayList<LiteColumn>();

	public static LiteSchema getSchema(Connection sqlConnection, String schema, String sql) throws SQLException  {
		try (Statement sqlStatement = sqlConnection.createStatement();) {
			ResultSet rs = sqlStatement.executeQuery(sql);
			return build(schema, rs.getMetaData());
		}
	}
	
	public static LiteSchema build(String schema, ResultSetMetaData meta) throws SQLException {
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
	
	public String generateCreateTable() throws SQLException {
		return generateCreateTable(defaultTypeMap);
	}
	
	public String generateCreateTable(Map<JDBCType, String> typeMap) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + table + " (\n");
		for (LiteColumn c : getColumns()) {
			sql.append("\t\"" + c.getName() + "\"");
			String typeName = typeMap.get(c.getType());
			if (typeName==null) {
				throw new SQLException("Sql type is not mapped - type:[" + c.getType() + "]");
			}
			sql.append(" " + typeName );
			if (c.getSize()<Integer.MAX_VALUE && "CHAR".equals(typeName.toUpperCase())) {
				sql.append(" (" +  c.getSize() +")");	
			}
			if (c.getSize()<Integer.MAX_VALUE && "VARCHAR".equals(typeName.toUpperCase())) {
				sql.append(" (" +  c.getSize() +")");	
			}
			
			if (!c.isNullable()) {
				sql.append(" NOT NULL");	
			}
			sql.append(",\n");
		}
		//remove \n
		sql = sql.deleteCharAt(sql.length()-1);
		//remove last comma
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
