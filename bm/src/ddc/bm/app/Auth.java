package ddc.bm.app;

import java.security.NoSuchAlgorithmException;

import ddc.bm.conf.Feature;
import ddc.bm.conf.UserConf;
import ddc.bm.conf.UserConfReader;
import ddc.bm.conf.UsersConf;
import ddc.bm.model.User;
import ddc.bm.servlet.Tenant;
import ddc.core.crypto.Crypto;
import ddc.core.crypto.Token;
import ddc.core.crypto.TokenException;

///ToDo transform to singleton
public class Auth {
	
	// private static final String LOG_INFO = "Auth - ";
	private static UserConfReader conf = new UserConfReader();

	public static Auth instance() {
		return new Auth();
	}

	public void recycle() {
		conf.reload();
	}
	
	public static User getUser(String domain, String token) throws NoSuchAlgorithmException, TokenException {
		final Tenant tenant = new Tenant(domain);
		Crypto c =  new Crypto(tenant.getCryptoConfig());
		String toks[] = Token.decodeToken(c, token);
		String username = toks[0];
		
		UsersConf usersConf = conf.getUsers(tenant.getName().toString());
		if (usersConf == null)
			throw new TokenException("Users configuration not found - domain:[" + domain + "]");
		
		UserConf userConf =usersConf.get(username);
		
		if (userConf == null)
			throw new TokenException("User configuration not found - user:[" + username + "]");

		return new User(tenant, username, userConf);		
	}
	
	public static String getToken(String domain, String username, String password) throws NoSuchAlgorithmException, TokenException {
		final Tenant tenant = new Tenant(domain);
		UsersConf usersConf = conf.getUsers(tenant.getName().toString());
		if (usersConf == null)
			throw new AuthException("Users configuration not found - domain:[" + domain + "]");
		UserConf userConf =usersConf.get(username);
		if (userConf == null)
			throw new AuthException("User configuration not found - user:[" + username + "]");
		if (!userConf.password.equals(password))
			throw new AuthException("Password not valide - user:[" + username + "]");
		Crypto c =  new Crypto(tenant.getCryptoConfig());
		return Token.encodeToken(c, username, password);
	}
	
//	public static User getAuthUser(String domain, String username, String password) throws NoSuchAlgorithmException, TokenException {
//		final Tenant tenant = new Tenant(domain);
//		UsersConf usersConf = conf.getUsers(tenant.getName().toString());
//		if (usersConf == null)
//			throw new AuthException("Users configuration not found - domain:[" + domain + "]");
//		UserConf userConf =usersConf.get(username);
//		if (userConf == null)
//			throw new AuthException("User configuration not found - user:[" + username + "]");
//		if (!userConf.password.equals(password))
//			throw new AuthException("Password not valide - user:[" + username + "]");
//		
//		return new User(username);
//	}
	

	
//	public boolean isFeatureEnabled(String tenant, String username, String feature) {
//	UserConf u = getUser(tenant, username);
//	if (u == null)
//		return false;
//	Feature f = u.features.get(feature);
//	if (f == null)
//		return false;
//	long now = System.currentTimeMillis();
//	return f.getPeriod().contains(now);
//}
	
//	public boolean isUserAuthenticated(String tenant, String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, EncoderException {
//		if (StringUtils.isBlank(tenant) || StringUtils.isBlank(username) || StringUtils.isBlank(password))
//			return false;
//		UserConf u = getUser(tenant, username);
//		if (u == null)
//			return false;
//
////		String token = Token.encodeToken(getCrypto(), tenant, username, password);
////		System.out.println(token);
//
//		return password.equals(u.password);
//	}

//	public boolean isUserAuthenticated(String tenant, String token) throws NoSuchAlgorithmException, UnsupportedEncodingException, DecoderException, EncoderException, TokenException {
//		if (StringUtils.isBlank(tenant) || StringUtils.isBlank(token))
//			return false;
//		
//		String toks[] = Token.decodeToken(getCrypto(), token);
//		if (toks == null)
//			return false;
//		if (toks.length != 4)
//			return false;
//		if (tenant.equals(toks[0]))
//			return isUserAuthenticated(toks[0], toks[1], toks[2]);
//		return false;
//	}

//	public String encodeToken(String tenant, String username, String password) throws NoSuchAlgorithmException, TokenException {
//		return Token.encodeToken(getCrypto(), tenant, username, password);
//	}	
	
	//TODO Check value in token
//	public String[] decodeToken(EnvType tenant, String token) throws NoSuchAlgorithmException, TokenException {
//		return Token.decodeToken(getCrypto(), token);
//	}	
	
//	public void checkToken(String token) throws NoSuchAlgorithmException, TokenException {
//		Token.decodeToken(getCrypto(), token);
//	}
	


//	private Crypto getCrypto() throws NoSuchAlgorithmException {
//		return new Crypto(new AESCryptoConfig("KeTQvRIFBlDuJUhslStQAw==", 128, "bRK+VCKvZ+D0qSzSJ9pbEg=="));
//	}

//	private UserConf getUser(String tenant, String username) {
//		UsersConf u = conf.getUsers(tenant);
//		if (u != null)
//			return u.get(username);
//		return null;
//	}


}
