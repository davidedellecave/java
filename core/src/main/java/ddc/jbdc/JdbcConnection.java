package ddc.jbdc;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class JdbcConnection {
	private JdbcConfiguration conf;
	public JdbcConnection() {}
	
	public JdbcConnection(JdbcConfiguration conf) {
		this.conf=conf;
	}
	public JdbcConfiguration getJdbcConnection() {
		return conf;
	}
	
	public abstract String getUrl(JdbcConfiguration conf);
	public String getUrl() {
		return getUrl(conf);
	}

	public abstract void checkConnection(Connection connection) throws SQLException;

	public abstract String getDefaultDriver();

	public abstract int getDefaultPort();

	public Connection createConnection(JdbcConfiguration conf) throws SQLException, ClassNotFoundException {
		String url = getUrl(conf);
		return SqlConnectionUtils.getNewConnection(url, conf.user, conf.password);
	}
}
