package ddc.core.schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author davidedc
 */
public class _LiteTable {
	private _LiteSchema schema = null;
	private List<LiteRow> rows = new ArrayList<LiteRow>();
	
	public _LiteSchema getSchema() {
		return schema;
	}
	public void setSchema(_LiteSchema schema) {
		this.schema = schema;
	}
	public List<LiteRow> getRows() {
		return rows;
	}
	public void setRows(List<LiteRow> rows) {
		this.rows = rows;
	}
	
	public static _LiteTable build(String schema, ResultSet rs) throws SQLException {
		_LiteTable t = new _LiteTable();
		t.setSchema(new _LiteSchema());
		t.setSchema(_LiteSchema.build(schema, rs.getMetaData()));
		while (rs.next()) {
			t.rows.add(LiteRow.build(rs));
		}
		return t;
	}
}
