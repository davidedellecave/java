package ddc.task.impl;

import java.sql.Connection;
import java.sql.SQLException;

import ddc.jdbc.JdbcConnectionFactory;
import ddc.jdbc.PooledDatasourceFactory;
import ddc.task.Task;

public abstract class DatasourceTask extends Task {
	private static PooledDatasourceFactory dsFactory = null;

	public PooledDatasourceFactory getDatasourceFactory() {
		if (dsFactory == null) {
			dsFactory = new PooledDatasourceFactory();
		}
		return dsFactory;
	}

	public Connection getConnection(JdbcConnectionFactory connFactory) throws SQLException {
		return getDatasourceFactory().createDataSource(connFactory).getConnection();
	}

	public void closeConnection(Connection conn) throws SQLException {
		if (conn != null && !conn.isClosed())
			conn.close();
	}

	public void commitAndCloseConnection(Connection conn) throws SQLException {
		if (conn != null && !conn.isClosed())
			conn.commit();
		if (conn != null && !conn.isClosed())
			conn.close();
	}
}
