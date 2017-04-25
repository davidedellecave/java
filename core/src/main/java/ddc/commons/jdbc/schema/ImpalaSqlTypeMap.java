package ddc.commons.jdbc.schema;

import java.sql.JDBCType;
import java.util.Map;
import java.util.TreeMap;

public class ImpalaSqlTypeMap {
	public static Map<JDBCType, String> map= new TreeMap<>();
	static {
		map.put(JDBCType.ARRAY, "ARRAY");
		map.put(JDBCType.BIGINT, "BIGINT");
		map.put(JDBCType.BINARY, "_BINARY_");
		map.put(JDBCType.BIT, "BOOLEAN");
		map.put(JDBCType.BLOB, "_BLOB_");
		map.put(JDBCType.BOOLEAN, "BOOLEAN");
		map.put(JDBCType.CHAR, "CHAR");
		map.put(JDBCType.CLOB, "VARCHAR");
		map.put(JDBCType.DATALINK, "_DATALINK_");
		map.put(JDBCType.DATE, "TIMESTAMP");
		map.put(JDBCType.DECIMAL, "DECIMAL");
		map.put(JDBCType.DISTINCT, "_DISTINCT_");
		map.put(JDBCType.DOUBLE,  "DOUBLE");
		map.put(JDBCType.FLOAT,  "FLOAT");
		map.put(JDBCType.INTEGER, "INT");
		map.put(JDBCType.JAVA_OBJECT,  "");
		map.put(JDBCType.LONGNVARCHAR,  "VARCHAR");
		map.put(JDBCType.LONGVARBINARY,  "VARCHAR");
		map.put(JDBCType.NCHAR,  "CHAR");
		map.put(JDBCType.NCLOB,  "VARCHAR");
		map.put(JDBCType.NULL,  "_NULL_");
		map.put(JDBCType.NUMERIC,  "DECIMAL");
		map.put(JDBCType.NVARCHAR,  "STRING");
		map.put(JDBCType.OTHER,  "_OTHER_");
		map.put(JDBCType.REAL,  "REAL");
		map.put(JDBCType.REF,  "_REF_");
		map.put(JDBCType.REF_CURSOR, "_REF_CURSOR_");
		map.put(JDBCType.ROWID, "_ROWID_");
		map.put(JDBCType.SMALLINT,"SMALLINT");
		map.put(JDBCType.SQLXML, "_SQLXML_");
		map.put(JDBCType.STRUCT, "_STRUCT_");
		map.put(JDBCType.TIME, "TIMESTAMP");
		map.put(JDBCType.TIMESTAMP, "TIMESTAMP");
		map.put(JDBCType.TIME_WITH_TIMEZONE, "TIMESTAMP");
		map.put(JDBCType.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP");
		map.put(JDBCType.TINYINT,"TINYINT");
		map.put(JDBCType.VARBINARY, "VARCHAR");
		map.put(JDBCType.VARCHAR, "STRING");
	}
}
