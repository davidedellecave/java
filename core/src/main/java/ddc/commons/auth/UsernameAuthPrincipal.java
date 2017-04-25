package ddc.commons.auth;

public class UsernameAuthPrincipal extends AuthPrincipal {
	private static final long serialVersionUID = 1L;
	
	private String PROPNAME_USERNAME="auth.username";

	public void setUsername(String username) {
		this.setProperty(PROPNAME_USERNAME, username);
	}

	public String getUsername() {
		return this.getProperty(PROPNAME_USERNAME);
	}

}
