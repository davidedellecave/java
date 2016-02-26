package ddc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author davidedc
 */
public class Table {
	private String name;
	private List<String> columns = new ArrayList<String>();
	private List<TableRow> rows = new ArrayList<TableRow>();
	
	public Table(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<TableRow> getRows() {
		return rows;
	}

	public void setRows(List<TableRow> rows) {
		this.rows = rows;
	}
	
	
}
