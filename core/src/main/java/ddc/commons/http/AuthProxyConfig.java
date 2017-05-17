package ddc.core.http;

public class AuthProxyConfig extends ProxyConfig {
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public AuthProxyConfig setUsername(String username) {
		this.username = username;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public AuthProxyConfig setPassword(String password) {
		this.password = password;
		return this;
	}
	
	

}
