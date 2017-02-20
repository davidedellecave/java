package ddc.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class HashUtil {
	public static int hash(String[] fields) {
		String j = StringUtils.join(fields, ' ');
		String[] toks = StringUtils.split(j,' ');
		Arrays.sort(toks);
		int h = Arrays.hashCode(toks);
		return h;
	}
	
	public static int hash(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		String toks[] = new String[meta.getColumnCount()];
		for (int i = 1; i<=meta.getColumnCount(); i++) {
			toks[i] = rs.getString(i);			
		}
		return hash(toks);
	}
}
