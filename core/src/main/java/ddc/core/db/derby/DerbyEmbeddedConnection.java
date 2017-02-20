package ddc.core.db.derby;

import java.sql.JDBCType;
import java.util.Map;

import ddc.jdbc.JdbcConfig;
import ddc.jdbc.JdbcConnectionFactory;

/**
 * @author davidedc 2014
 *
 */
public class DerbyEmbeddedConnection extends JdbcConnectionFactory {

	public DerbyEmbeddedConnection(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		return "jdbc:derby:" + getDatabase() + ";create=true";
	}

	@Override
	public String getDriver() {
		return "org.apache.derby.jdbc.EmbeddedDriver";
	}

	@Override
	public int getDefaultPort() {
		return 0;
	}

	@Override
	public String getSqlLimitTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<JDBCType, String> getSqlTypeMap() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void checkConnection(Connection connection) throws SQLException {
//		SqlConnectionUtils.select(connection, "SELECT 1");
//		
//	}

}
