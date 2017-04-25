package ddc.commons.auth;

public class KerberosAuthPrincipal extends AuthPrincipal {
	private static final long serialVersionUID = 1L;
	private String PROPNAME_PRINCIPAL="kerberos.username";

	public void setUsername(String username) {
		this.setProperty(PROPNAME_PRINCIPAL, username);
	}

	public String getUsername() {
		return this.getProperty(PROPNAME_PRINCIPAL);
	}
}
