package ddc.auth;

import java.util.Properties;

public class AuthPrincipal extends Properties {
	private static final long serialVersionUID = 1L;
	private boolean authenticated = false;
	private String authenticationMessage = "";

	public String getAuthenticationMessage() {
		return authenticationMessage;
	}

	public void setAuthenticationMessage(String authenticationMessage) {
		this.authenticationMessage = authenticationMessage;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public boolean isNotAuthenticated() {
		return !authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

}
