package ddc.jdbc;

public class PostgreSQLFactory extends JdbcConnectionFactory {

	public PostgreSQLFactory(JdbcConfig conf) {
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
