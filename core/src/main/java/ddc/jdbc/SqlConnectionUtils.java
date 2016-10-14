package ddc.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class SqlConnectionUtils {
	private static Logger logger = Logger.getLogger(SqlConnectionUtils.class);

	public static boolean testConnection(Connection connection) throws SQLException {
		String sql = "SELECT 1";
		select(connection, sql);
		return true;
	}

	private final static int RS_TYPE = ResultSet.TYPE_FORWARD_ONLY;//.TYPE_SCROLL_INSENSITIVE;
	private final static int RS_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
	public static ResultSet select(Connection connection, String sql) throws SQLException {
		Statement statement = null;
		ResultSet rs = null;
		statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
		rs = statement.executeQuery(sql);
		statement.close();
		System.out.println("Query executed: " + sql);
		return rs;
	}
	
	public static void printTable(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		for (int i=1; i<=meta.getColumnCount(); i++) {
			System.out.print(meta.getColumnName(i) + " | ");			
		}
		System.out.println();
		System.out.println("============================================================");
		while (rs.next()) {
			for (int i=1; i<=meta.getColumnCount(); i++) {
				System.out.print(rs.getString(i) + " | ");	
			}
			System.out.println();
		}
	}
}
