package ddc.core.db.postgresql;

import java.sql.JDBCType;
import java.util.Map;
import java.util.TreeMap;


public class PostreSqlTypeMap {
	public static Map<JDBCType, String> map= new TreeMap<>();
	static {
		map.put(JDBCType.ARRAY, "_ARRAY_");
		map.put(JDBCType.BIGINT, "BIGINT");
		map.put(JDBCType.BINARY, "BINARY");
		map.put(JDBCType.BIT, "BOOLEAN");
		map.put(JDBCType.BLOB, "VARBINARY");
		map.put(JDBCType.BOOLEAN, "BOOLEAN");
		map.put(JDBCType.CHAR, "CHAR");
		map.put(JDBCType.CLOB, "LONG VARCHAR");
		map.put(JDBCType.DATALINK, "_DATALINK_");
		map.put(JDBCType.DATE, "DATE");
		map.put(JDBCType.DECIMAL, "DECIMAL");
		map.put(JDBCType.DISTINCT, "_DISTINCT_");
		map.put(JDBCType.DOUBLE,  "DOUBLE PRECISION");
		map.put(JDBCType.FLOAT,  "FLOAT");
		map.put(JDBCType.INTEGER, "INTEGER");
		map.put(JDBCType.JAVA_OBJECT,  "VARBINARY");
		map.put(JDBCType.LONGNVARCHAR,  "LONG VARCHAR");
		map.put(JDBCType.LONGVARBINARY,  "VARBINARY");
		map.put(JDBCType.NCHAR,  "CHAR");
		map.put(JDBCType.NCLOB,  "LONG VARCHAR");
		map.put(JDBCType.NULL,  "_NULL_");
		map.put(JDBCType.NUMERIC,  "NUMERIC");
		map.put(JDBCType.NVARCHAR,  "VARCHAR");
		map.put(JDBCType.OTHER,  "_OTHER_");
		map.put(JDBCType.REAL,  "REAL");
		map.put(JDBCType.REF,  "_REF_");
		map.put(JDBCType.REF_CURSOR, "_REF_CURSOR_");
		map.put(JDBCType.ROWID, "_ROWID_");
		map.put(JDBCType.SMALLINT,"SMALLINT");
		map.put(JDBCType.SQLXML, "VARCHAR");
		map.put(JDBCType.STRUCT, "_STRUCT_");
		map.put(JDBCType.TIME, "TIMESTAMP");
		map.put(JDBCType.TIMESTAMP, "TIMESTAMP");
		map.put(JDBCType.TIME_WITH_TIMEZONE, "TIMESTAMPTZ");
		map.put(JDBCType.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMPTZ");
		map.put(JDBCType.TINYINT,"TINYINT");
		map.put(JDBCType.VARBINARY, "VARBINARY");
		map.put(JDBCType.VARCHAR, "VARCHAR");
	}

}
