package ddc.jdbc;

public class VerticaDbConnection extends JdbcConnectionFactory {

	public VerticaDbConnection(JdbcConfig conf) {
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

}
