package ddc.core.db.postgresql;

import ddc.jdbc.JdbcConfig;
import ddc.jdbc.JdbcConnectionFactory;

public class PostgreSqlFactory extends JdbcConnectionFactory {

	public PostgreSqlFactory(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "org.postgresql.Driver";
	}

	@Override
	public int getDefaultPort() {
		return 5432;
	}
}
