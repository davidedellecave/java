package ddc.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author davidedc 2014
 *
 */
//public class LiteTable extends ArrayList<LiteRow>{
public class LiteTable {
	private List<LiteRow> rows = new ArrayList<LiteRow>(); 
	
	public void setRows(List<LiteRow> rows) {
		this.rows=rows;
	}

	public List<LiteRow> getRows() {
		return rows;
	}

	/**
	 * Remove all rows and add one row using specified fields
	 * @param fields
	 */
	public void setFields(LiteFields fields) {
		this.rows.clear();
		addFields(fields);
	}
	
	/*
	 * Add row using specified fields
	 */
	public void addFields(LiteFields fields) {
		LiteRow row = new LiteRow();
		row.setFields(fields);
		this.rows.add(row);
	}
	
	public long getSize() {
		return rows.size();
	}
}
