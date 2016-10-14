package ddc.core.db.db2;

import ddc.jdbc.JdbcConfig;
import ddc.jdbc.JdbcConnectionFactory;

//IBM DB2 Universal Driver Type 4 
//
//--------------------------------------------------------------------------------
//
//DRIVER CLASS: com.ibm.db2.jcc.DB2Driver 
//
//DRIVER LOCATION: db2jcc.jar and db2jcc_license_cu.jar 
//(Both of these jars must be included) 
//
//JDBC URL FORMAT: jdbc:db2://<host>[:<port>]/<database_name> 
//
//JDBC URL Examples: 
//
//jdbc:db2://127.0.0.1:50000/SAMPLE 

public class Db2Connection extends JdbcConnectionFactory {

	public Db2Connection(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		return "jdbc:db2://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.ibm.db2.jcc.DB2Driver";
	}

	@Override
	public int getDefaultPort() {
		return 3003;
	}

	@Override
	public String getSqlLimitTemplate() {
		// TODO Auto-generated method stub
		return null;
	}
	

//	@Override
//	public void checkConnection(Connection connection) throws SQLException {
//		String sql = "SELECT * FROM SYSIBM.SYSTABLES fetch first 5 rows only" ;
//		ResultSet table = SqlConnectionUtils.select(connection, sql);
//		SqlConnectionUtils.printTable(table);
//	}
}
