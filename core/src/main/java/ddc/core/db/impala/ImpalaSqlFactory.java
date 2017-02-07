package ddc.core.db.impala;

import ddc.jdbc.JdbcConfig;
import ddc.jdbc.JdbcConnectionFactory;

public class ImpalaSqlFactory extends JdbcConnectionFactory {

	public ImpalaSqlFactory(JdbcConfig conf) {
		super(conf);
	}
	
	@Override
	public String getUrl() {
		return "jdbc:impala://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.cloudera.impala.jdbc4.Driver";
	}

	@Override
	public int getDefaultPort() {
		return 21050;
	}

	@Override
	public String getSqlLimitTemplate() {
		return "SELECT $COLUMNS FROM $TABLE LIMIT $MAXROWS";
	}

}
