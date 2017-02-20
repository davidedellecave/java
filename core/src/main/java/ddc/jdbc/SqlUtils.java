package ddc.jdbc;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ddc.util.Chronometer;

public class SqlUtils {
	private static Logger logger = Logger.getLogger(SqlUtils.class);
	
	// public static boolean testConnection(Connection connection) throws
	// SQLException {
	// String sql = "SELECT 1";
	// select(connection, sql);
	// return true;
	// }
	//
	// <property name="queryLimitTemplate" value="SELECT $COLUMNS FROM $TABLE
	// LIMIT $MAXROWS"/>
	// <property name="queryLimitTemplate" value="SELECT $COLUMNS FROM $TABLE
	// WHERE WHERE RowNum <= $MAXROWS"/>
	// <property name="queryLimitTemplate" value="SELECT TOP $MAXROWS $COLUMNS
	// FROM $TABLE"/>

	private final static int RS_TYPE = ResultSet.TYPE_FORWARD_ONLY;// .TYPE_SCROLL_INSENSITIVE;
	private final static int RS_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
	private final static int FETCH_SIZE = 100000;
	private final static int VERBOSE_COUNT = 10;

	public static void select(Connection connection, String sql, SqlRowHandler handler) throws Exception {
		logger.debug("Executing... sql:[" + sql + "]");
		Statement statement = null;
		ResultSet rs = null;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.setFetchSize(FETCH_SIZE);
			rs = statement.executeQuery(sql);
			// ResultSetMetaData meta = rs.getMetaData();
			long counter = 0;
			while (rs.next()) {
				counter++;
				handler.handle(counter, rs);
				if (logger.isDebugEnabled() && counter <= VERBOSE_COUNT) {
					printHandler(counter, rs, 40);
					if (counter == VERBOSE_COUNT)
						logger.debug("more rows....");
				}
			}
			logger.info("Executed - sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		}
	}

	public static String selectOneField(Connection connection, String sql) throws Exception {
		logger.debug("Executing... sql:[" + sql + "]");
		Statement statement = null;
		ResultSet rs = null;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.setFetchSize(FETCH_SIZE);
			rs = statement.executeQuery(sql);
			// ResultSetMetaData meta = rs.getMetaData();
			String result = null;
			if (rs.next()) {
				result = rs.getString(1);
			}
			logger.info("Executed - sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
			return result;
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		}
	}

	public static void execute(Connection connection, String sql) throws SQLException {
		logger.debug("Executing... catalog:[" + connection.getCatalog() + "] sql:[" + sql + "]");
		Statement statement = null;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.execute(sql);
			logger.info("Executed - catalog:[" + connection.getCatalog() + "] sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
		}
	}

	static void printHandler(long counter, ResultSet rs, int colSize) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		String message = "";
		if (counter == 1) {
			meta = rs.getMetaData();
			String sep = "";
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				String typeName = JDBCType.valueOf(meta.getColumnType(i)).getName();
				String col = StringUtils.rightPad(meta.getColumnName(i) + "(" + typeName + ")", colSize);
				message += col + " |";
				sep += StringUtils.repeat("=", colSize) + " |";
			}
			logger.debug(message);
			logger.debug(sep);
		}
		message = "";
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			String v = rs.getString(i);
			if (v == null)
				v = "NULL";
			String col = StringUtils.rightPad(v, colSize);
			message += col + " |";
		}
		logger.debug(message);
	}

	public static String getRowInfo(ResultSetMetaData meta) throws SQLException {
		StringBuilder b = new StringBuilder();
		if (StringUtils.isNotBlank( meta.getTableName(1))) {
			b.append("Sql schema - table:[" + meta.getTableName(1) + "]\n");
		} else {
			b.append("Sql schema\n");
		}
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			b.append("\t#:[" + i + "] name:[" + meta.getColumnName(i) + "] type:[" + JDBCType.valueOf(meta.getColumnType(i)).getName() + "]");
			b.append('\n');
		}
		return b.toString();
	}

	public static String getRowInfo(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		StringBuilder b = new StringBuilder();
		if (StringUtils.isNotBlank( meta.getTableName(1))) {
			b.append("Sql row - table:[" + meta.getTableName(1) + "]\n");
		} else {
			b.append("Sql row\n");
		}
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			b.append("\t#:[" + i + "] name:[" + meta.getColumnName(i) + "] type:[" + JDBCType.valueOf(meta.getColumnType(i)).getName() + "]");
			b.append(" value:[" + String.valueOf(rs.getObject(i)) + "]");
			b.append('\n');
		}
		return b.toString();
	}

	public static void printSqlSelect(Connection connection, String sql, final OutputStream ostream, final int colSize) throws Exception {
		printSqlSelect(connection, sql, new PrintStream(ostream), colSize);
	}

	public static void printSqlSelect(Connection connection, String sql, final PrintStream ps, final int colSize) throws Exception {
		select(connection, sql, new SqlRowHandler() {
			ResultSetMetaData meta = null;

			@Override
			public void handle(long counter, ResultSet rs) throws SQLException {
				if (counter == 1) {
					meta = rs.getMetaData();
					String sep = "";
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						String typeName = JDBCType.valueOf(meta.getColumnType(i)).getName();
						String col = StringUtils.rightPad(meta.getColumnName(i) + "(" + typeName + ")", colSize);
						ps.print(col + " |");
						sep += StringUtils.repeat("=", colSize) + " |";
					}
					ps.println();
					ps.println(sep);
				}
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String v = rs.getString(i);
					if (v == null)
						v = "NULL";
					String col = StringUtils.rightPad(v, colSize);
					ps.print(col + " |");
				}
				ps.println();
			}
		});
	}
}
