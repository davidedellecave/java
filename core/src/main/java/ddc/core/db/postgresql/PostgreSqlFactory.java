package ddc.core.db.postgresql;

import java.sql.JDBCType;
import java.util.Map;

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

	@Override
	public String getSqlLimitTemplate() {
		return "SELECT $COLUMNS FROM $TABLE LIMIT $MAXROWS";
	}

	@Override
	public Map<JDBCType, String> getSqlTypeMap() {
		return PostreSqlTypeMap.map;
	}
}
