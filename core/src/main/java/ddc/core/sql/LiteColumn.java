package ddc.core.sql;

import java.sql.JDBCType;

public class LiteColumn {
	private String name;
	private JDBCType type;
	private boolean isNullable;
	private int size;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JDBCType getType() {
		return type;
	}
	public void setType(JDBCType type) {
		this.type = type;
	}
	public boolean isNullable() {
		return isNullable;
	}
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}


	
}
