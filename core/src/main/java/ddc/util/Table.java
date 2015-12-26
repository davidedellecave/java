package ddc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author davidedc
 */
public class Table {
	private String name;
	private List<String> columns = new ArrayList<String>();
	private List<Row> rows = new ArrayList<Row>();
	
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

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	
	
}
