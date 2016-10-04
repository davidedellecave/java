package ddc.core.sql;

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
}
