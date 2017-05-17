package ddc.core.http;

public class ProxyConfig {
	private String host="";
	private int port=0;
	private String protocol="http";
	
	public String getHost() {
		return host;
	}
	public ProxyConfig setHost(String host) {
		this.host = host;
		return this;
	}
	public int getPort() {
		return port;
	}
	public ProxyConfig setPort(int port) {
		this.port = port;
		return this;
	}
	public String getProtocol() {
		return protocol;
	}
	public ProxyConfig setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}
	
	
}
