package ddc.core.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author davidedc, 27/set/2010
 */
public class LiteRow extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;
	
	public static LiteRow build(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		LiteRow row = new LiteRow();
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			row.add(rs.getObject(i));
		}		
		return row;
	}
	
}
