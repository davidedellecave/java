package ddc.jbdc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author davidedc 2014
 *
 */
public class DerbyEmbeddedConnection extends JdbcConnection {

//	String driver = "org.apache.derby.jdbc.EmbeddedDriver";
//	try {
//	    Class.forName(driver); 
//	} catch(java.lang.ClassNotFoundException e) {
//	  System.err.println(e.getMessage());
//	  return;
//	}
//	System.out.println("Driver loaded");
//	
//	String connectionURL = "jdbc:derby:@dbName;create=true";
	
	@Override
	public String getUrl(JdbcConfiguration conf) {
		return "jdbc:derby:" + conf.database + ";create=true";		
	}

	@Override
	public void checkConnection(Connection connection) throws SQLException {
		SqlConnectionUtils.select(connection, "SELECT 1");
		
	}

	@Override
	public String getDefaultDriver() {
		return "org.apache.derby.jdbc.EmbeddedDriver";
	}

	@Override
	public int getDefaultPort() {
		return 0;
	}

}
