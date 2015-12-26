package ddc.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class LiteRowMapper {

	public LiteRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		LiteRow row = new LiteRow();
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			String name = meta.getColumnName(i);
			String value = rs.getString(name);
			row.getFields().put(name, value);
		}		
		return row;
	}
}
