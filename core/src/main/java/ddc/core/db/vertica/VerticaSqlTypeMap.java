package ddc.core.db.vertica;

import java.sql.JDBCType;
import java.util.Map;
import java.util.TreeMap;

public class VerticaSqlTypeMap {
	public static Map<JDBCType, String> map= new TreeMap<>();
	static {
		map.put(JDBCType.ARRAY, "");
		map.put(JDBCType.BIGINT, "");
		map.put(JDBCType.BINARY, "");
		map.put(JDBCType.BIT, "");
		map.put(JDBCType.BLOB, "");
		map.put(JDBCType.BOOLEAN, "");
		map.put(JDBCType.CHAR, "");
		map.put(JDBCType.CLOB, "");
		map.put(JDBCType.DATALINK, "");
		map.put(JDBCType.DATE, "");
		map.put(JDBCType.DECIMAL, "");
		map.put(JDBCType.DISTINCT, "");
		map.put(JDBCType.DOUBLE,  "");
		map.put(JDBCType.FLOAT,  "");
		map.put(JDBCType.INTEGER, "");
		map.put(JDBCType.JAVA_OBJECT,  "");
		map.put(JDBCType.LONGNVARCHAR,  "");
		map.put(JDBCType.LONGVARBINARY,  "");
		map.put(JDBCType.NCHAR,  "");
		map.put(JDBCType.NCLOB,  "");
		map.put(JDBCType.NULL,  "");
		map.put(JDBCType.NUMERIC,  "");
		map.put(JDBCType.NVARCHAR,  "");
		map.put(JDBCType.OTHER,  "");
		map.put(JDBCType.REAL,  "");
		map.put(JDBCType.REF,  "");
		map.put(JDBCType.REF_CURSOR, "");
		map.put(JDBCType.ROWID, "");
		map.put(JDBCType.SMALLINT,"");
		map.put(JDBCType.SQLXML, "");
		map.put(JDBCType.STRUCT, "");
		map.put(JDBCType.TIME, "");
		map.put(JDBCType.TIME_WITH_TIMEZONE, "");
		map.put(JDBCType.TIMESTAMP_WITH_TIMEZONE, "");
		map.put(JDBCType.TINYINT,"");
		map.put(JDBCType.VARBINARY, "");
		map.put(JDBCType.VARCHAR, "");
	}
}
