package ddc.core.db.oracle;

import ddc.jdbc.JdbcConfig;
import ddc.jdbc.JdbcConnectionFactory;

public class OracleConnection extends JdbcConnectionFactory {

	public OracleConnection(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		return "jdbc:oracle:thin:@" + getHost() + ":" + getPort() + ":" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	public int getDefaultPort() {
		return 1521;
	}

	@Override
	public String getSqlLimitTemplate() {
		return "SELECT $COLUMNS FROM $TABLE WHERE ROWNUM<=$MAXROWS";
	}
	
//	@Override
//	//jdbc:oracle:thin:<database_name>@<server>[:<1521>]:SID
//	//jdbc:oracle:thin:@vm-test:1521:XE
//	//SID (Service Identifier)Example: XE, ORACLE, dbname
//	public String getUrl(JdbcConfiguration conf) {
//		return "jdbc:oracle:thin:@" + conf.host + ":" + conf.port + ":" + conf.database;
//	}
//
//	@Override
//	public void checkConnection(Connection connection) throws SQLException {
//		String sql = "SELECT count(*) AS TOT FROM sys.all_tables";
//		SqlConnectionUtils.select(connection, sql);
//	}
//
//	@Override
//	public String getDefaultDriver() {
//		return "oracle.jdbc.driver.OracleDriver";
//	}
//
//	@Override
//	public int getDefaultPort() {
//		return 1521;
//	}
//	
}
