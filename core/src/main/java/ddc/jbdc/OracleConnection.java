package ddc.jbdc;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleConnection extends JdbcConnection{

	@Override
	//jdbc:oracle:thin:<database_name>@<server>[:<1521>]:SID
	//jdbc:oracle:thin:@vm-test:1521:XE
	//SID (Service Identifier)Example: XE, ORACLE, dbname
	public String getUrl(JdbcConfiguration conf) {
		return "jdbc:oracle:thin:@" + conf.host + ":" + conf.port + ":" + conf.database;
	}

	@Override
	public void checkConnection(Connection connection) throws SQLException {
		String sql = "SELECT count(*) AS TOT FROM sys.all_tables";
		SqlConnectionUtils.select(connection, sql);
	}

	@Override
	public String getDefaultDriver() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	public int getDefaultPort() {
		return 1521;
	}
	
}
