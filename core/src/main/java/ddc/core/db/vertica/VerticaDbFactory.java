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
		return "SELECT $COLUMNS FROM $TABLE LIMIT $MAXROWS";
	}

	
	public static void main(String[] args) {
		JdbcConfig c = new JdbcConfig();
		c.setHost("vertica-infohub.mondadori.it");
		c.setPort(5433);
		c.setDatabase("infohub");
		c.setUser("dbadmin");
		c.setPassword("yFj#wJ85");
		VerticaDbFactory f = new VerticaDbFactory(c);
		System.out.println(f.getUrl());
		
		
//		{
//			  "type": "jdbc",
//			  "driver": "com.mysql.jdbc.Driver",
//			  "url": "jdbc:mysql://localhost:3306",
//			  "username": "root",
//			  "password": "mypassword",
//			  "enabled": true
//			}
	}
}
