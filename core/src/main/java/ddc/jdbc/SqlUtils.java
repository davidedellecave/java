package ddc.jdbc;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class SqlUtils {
	private static Logger logger = Logger.getLogger(SqlUtils.class);

	// public static boolean testConnection(Connection connection) throws
	// SQLException {
	// String sql = "SELECT 1";
	// select(connection, sql);
	// return true;
	// }

	private final static int RS_TYPE = ResultSet.TYPE_FORWARD_ONLY;// .TYPE_SCROLL_INSENSITIVE;
	private final static int RS_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
	private final static int FETCH_SIZE = 100000;

	public static void select(Connection connection, String sql, SqlRowHandler handler) throws Exception {
		logger.debug("Executing... sql:[" + sql + "]");
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.setFetchSize(FETCH_SIZE);
			rs = statement.executeQuery(sql);
			// ResultSetMetaData meta = rs.getMetaData();
			long counter = 0;
			while (rs.next()) {
				counter++;
				handler.handle(counter, rs);
			}
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		}
	}

	public static void printSqlSelect(Connection connection, String sql, PrintStream ps, int colSize) throws Exception {
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
						System.out.print(col + " |");
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
