package ddc.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ddc.util.Chronometer;

public abstract class JdbcConnectionFactory {
	private final static int DEFAULT_CONNECTION_RETRY = 1;
	private final static int DEFAULT_CONNECTION_WAIT = 30*1000;	
	private static Logger logger = Logger.getLogger(JdbcConnectionFactory.class);
	private boolean driverLoaded = false;
	private JdbcConfig conf;

	public JdbcConnectionFactory(JdbcConfig conf) {
		this.conf = conf;
	}

	public abstract String getUrl();

	public abstract String getDriver();

	public abstract int getDefaultPort();

	public abstract String getSqlLimitTemplate();

	public String getHost() {
		return conf.getHost();
	}

	public int getPort() {
		return conf.getPort() > 0 ? conf.getPort() : getDefaultPort();
	}

	public String getDatabase() {
		return conf.getDatabase();
	}

	public String getUser() {
		return conf.getUser();
	}

	public String getPassword() {
		return conf.getPassword();
	}

	public String getSqlLimit(String table, String columns, int limit) {
		String s = getSqlLimitTemplate();
		s = s.replace("$TABLE", table);
		s = s.replace("$COLUMNS", columns);
		s = s.replace("$MAXROWS", String.valueOf(limit));
		return s;
	}

	public Connection createConnection() throws SQLException, ClassNotFoundException {
		loadDriver();
		Connection c = DriverManager.getConnection(getUrl(), conf.getUser(), conf.getPassword());
		logger.debug("Sql connection created:[" + getUrl() + "] user:[" + conf.getUser() + "]");
		return c;
	}

	public Connection createConnection(int retry, long waitMillis) throws SQLException, ClassNotFoundException {
		retry = retry > 0 ? retry : DEFAULT_CONNECTION_RETRY;
		waitMillis = waitMillis > 0 ? waitMillis : DEFAULT_CONNECTION_WAIT;
		int counter = 0;
		Throwable exception = null;
		while (counter < retry) {
			try {
				if (counter > 0)
					Chronometer.sleep(waitMillis);
				Connection conn = createConnection();				
				return conn;
			} catch (Throwable e) {
				counter++;
				logger.warn("Connection failed - connection:[" + this.toString() + "] \n\t exception:[" + e.getMessage() + "] \n\t retry... " + counter + "/" + retry);
				exception = e;
			}
		}
		throw new SQLException("Cannection cannot be established after " + retry + " retries - connection:[" + this.toString() + "]", exception);
	}

	public void loadDriver() throws ClassNotFoundException {
		if (driverLoaded)
			return;
		Class.forName(getDriver());
		logger.debug("Sql driver loaded:[" + getDriver() + "]");
		driverLoaded = true;
	}

	public static void close(PreparedStatement statement) {
		if (statement != null) {
			try {
				if (!statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				if (!statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					logger.debug("Sql connection closing - catalog:[" + connection.getCatalog() + "]");
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		if (conf != null)
			return conf.toString();
		return super.toString();
	}
}
