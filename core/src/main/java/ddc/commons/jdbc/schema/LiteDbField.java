package ddc.commons.jdbc.schema;

public class LiteDbField<T> {
	private LiteDbColumn column;
	private T value;

	public LiteDbField(LiteDbColumn column, T value) {
		super();
		this.column = column;
		this.value = value;
	}

	public LiteDbColumn getColumn() {
		return column;
	}

	public T getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	public void setValue(T value) {
		this.value = (T) castToSqlType(value);
	}

	@Override
	public String toString() {
		return column.toString() + ":[" + String.valueOf(value) + "]";
	}

	private Object castToSqlType(T value) {
		if (value == null)
			return null;

		switch (column.getType()) {
		case CHAR:
		case VARCHAR:
		case LONGVARCHAR:
		case SQLXML:
		case ROWID:
		case REF_CURSOR:
		case REF:
		case OTHER:
		case NVARCHAR:
		case NCHAR:
		case LONGNVARCHAR:
		case DISTINCT:
			String s = String.valueOf(value);
			if (s.length() > column.getSize()) {
				s = s.substring(0, column.getSize());
			}
			return s;

		case NULL:
			return null;

		case NUMERIC:
		case DECIMAL:
			return Integer.valueOf(String.valueOf(value));

		case BIT:
		case BOOLEAN:
			return Boolean.valueOf(String.valueOf(value));

		case TINYINT:
			return Byte.valueOf(String.valueOf(value));

		case SMALLINT:
			return Byte.valueOf(String.valueOf(value));

		case INTEGER:
			return Integer.valueOf(String.valueOf(value));

		case BIGINT:
			return Long.valueOf(String.valueOf(value));

		case REAL:
		case FLOAT:
			return Float.valueOf(String.valueOf(value));

		case DOUBLE:
			return Double.valueOf(String.valueOf(value));

		case BINARY:
		case VARBINARY:
		case LONGVARBINARY:
		case STRUCT:
		case NCLOB:
		case JAVA_OBJECT:
		case DATALINK:
		case CLOB:
		case BLOB:
		case ARRAY:
			return String.valueOf(value).getBytes();

		case DATE:
			return java.sql.Date.valueOf(String.valueOf(value));

		case TIME:
		case TIME_WITH_TIMEZONE:
			return java.sql.Time.valueOf(String.valueOf(value));

		case TIMESTAMP:
		case TIMESTAMP_WITH_TIMEZONE:
			return java.sql.Timestamp.valueOf(String.valueOf(value));
		}

		return null;
	}
}
