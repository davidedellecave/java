package ddc.jdbc;

public class JdbcPoolConfig extends JdbcConfig {
	private int concurrentConnections = 1;

	public int getConcurrentConnections() {
		return concurrentConnections;
	}

	public void setConcurrentConnections(int concurrentConnections) {
		this.concurrentConnections = concurrentConnections;
	}


}