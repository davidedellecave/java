package ddc.core.schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author davidedc
 */
public class LiteTable {
	private LiteSchema schema = null;
	private List<LiteRow> rows = new ArrayList<LiteRow>();
	
	public LiteSchema getSchema() {
		return schema;
	}
	public void setSchema(LiteSchema schema) {
		this.schema = schema;
	}
	public List<LiteRow> getRows() {
		return rows;
	}
	public void setRows(List<LiteRow> rows) {
		this.rows = rows;
	}
	
	public static LiteTable build(String schema, ResultSet rs) throws SQLException {
		LiteTable t = new LiteTable();
		t.setSchema(new LiteSchema());
		t.setSchema(LiteSchema.build(schema, rs.getMetaData()));
		while (rs.next()) {
			t.rows.add(LiteRow.build(rs));
		}
		return t;
	}
}
