package ddc.core.db.vertica;

import ddc.jdbc.JdbcConfig;
import ddc.jdbc.JdbcConnectionFactory;

public class VerticaDbFactory extends JdbcConnectionFactory {

	public VerticaDbFactory(JdbcConfig conf) {
		super(conf);
	}
	
	@Override
	public String getUrl() {
		return "jdbc:vertica://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.vertica.jdbc.Driver";
	}

	@Override
	public int getDefaultPort() {
		return 5433;
	}

	@Override
	public String getSqlLimitTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

}
