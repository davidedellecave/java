package ddc.jbdc;

import java.sql.Connection;
import java.sql.SQLException;

public class MsSqlConnection extends JdbcConnection {

	public MsSqlConnection() {	}
	
	public MsSqlConnection(JdbcConfiguration conf) {
		super(conf);
	}
	
	@Override
	public String getUrl(JdbcConfiguration conf) {
            int port = conf.port;
            if (port==0) port = getDefaultPort();
		return "jdbc:sqlserver://" + conf.host + ":" + port + ";databaseName=" + conf.database;
	}

	@Override
	public void checkConnection(Connection connection) throws SQLException {
		SqlConnectionUtils.select(connection, "SELECT 1");
	}

	@Override
	public String getDefaultDriver() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	public int getDefaultPort() {
		return 1433;
	}

	
//	@Override
//	public Connection createConnection(JdbcConfiguration conf) throws SQLException,
//			ClassNotFoundException {
//			
//		String url = "jdbc:sqlserver://" + conf.host + ":" + conf.port + ";";
//		url += "databaseName=" + conf.database;
//		return ConnectionUtils.getNewConnection(url, conf.user, conf.password);
//	}
//
//	@Override
//	public int getDefaultPort() {
//		return 1433;
//	}
//
//	@Override
//	public String getDefaultDriverName() {	
//		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//	}
//
//	@Override
//	public void loadDriver() throws ClassNotFoundException {
//		ConnectionUtils.loadDriver(getDefaultDriverName());
//		
//	}
//
//	@Override
//	public void testConnection(Connection connection) throws SQLException {
//		String sql = "SELECT 1";
//		ConnectionUtils.select(connection, sql);
//		
//	}

}
