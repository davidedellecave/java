package ddc.commons.jdbc.schema;

import java.sql.JDBCType;

public class LiteDbColumn {
	private String name="";		
	private JDBCType type;
	private boolean isNullable;
	private int size = 0;
	private int index = 0;
	private String desc = null;

	public LiteDbColumn() {}
	
	public LiteDbColumn(String name, JDBCType type, boolean isNullable) {
		super();
		this.name = name;
		this.type = type;
		this.isNullable = isNullable;
		this.size = 0;
	}
	
	public LiteDbColumn(String name, JDBCType type, boolean isNullable, int size) {
		super();
		this.name = name;
		this.type = type;
		this.isNullable = isNullable;
		this.size = size;
	}

	public LiteDbColumn(String name, JDBCType type, boolean isNullable, int size, String desc) {
		super();
		this.name = name;
		this.type = type;
		this.isNullable = isNullable;
		this.size = size;
		this.desc = desc;
	}

	
	public LiteDbColumn(String name, JDBCType type, boolean isNullable, int size, int index) {
		super();
		this.name = name;
		this.type = type;
		this.isNullable = isNullable;
		this.size = size;
		this.index = index;
	}
	

	
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
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return name;
	}
}
