package ddc.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author davidedc 2014
 *
 */
//public class LiteTable extends ArrayList<LiteRow>{
public class _LiteTable {
	private List<_LiteRow> rows = new ArrayList<_LiteRow>(); 
	
	public void setRows(List<_LiteRow> rows) {
		this.rows=rows;
	}

	public List<_LiteRow> getRows() {
		return rows;
	}

	/**
	 * Remove all rows and add one row using specified fields
	 * @param fields
	 */
	public void setFields(_LiteFields fields) {
		this.rows.clear();
		addFields(fields);
	}
	
	/*
	 * Add row using specified fields
	 */
	public void addFields(_LiteFields fields) {
		_LiteRow row = new _LiteRow();
		row.setFields(fields);
		this.rows.add(row);
	}
	
	public long getSize() {
		return rows.size();
	}
}
