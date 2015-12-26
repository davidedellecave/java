package ddc.jbdc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


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

public class Db2Connection extends JdbcConnection {

	@Override
	public void checkConnection(Connection connection) throws SQLException {
		String sql = "SELECT * FROM SYSIBM.SYSTABLES fetch first 5 rows only" ;
		ResultSet table = SqlConnectionUtils.select(connection, sql);
		SqlConnectionUtils.printTable(table);
	}

	@Override
	public String getDefaultDriver() {
		return "com.ibm.db2.jcc.DB2Driver";
	}

	@Override
	public int getDefaultPort() {
		return 3003;
	}

	/*Example: jdbc:db2://zzose.int.cgu.it:3003/DB2T */
	@Override
	public String getUrl(JdbcConfiguration conf) {
		return "jdbc:db2://" + conf.host + ":" + conf.port + "/" + conf.database;
	}

}
