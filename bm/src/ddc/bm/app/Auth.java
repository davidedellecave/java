package ddc.bm.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.StringUtils;

import ddc.bm.conf.UserConfReader;
import ddc.bm.conf.Feature;
import ddc.bm.conf.UserConf;
import ddc.bm.conf.UsersConf;
import ddc.core.crypto.AESCryptoConfig;
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

	public boolean isUserAuthenticated(String tenant, String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, EncoderException {
		if (StringUtils.isBlank(tenant) || StringUtils.isBlank(username) || StringUtils.isBlank(password))
			return false;
		UserConf u = getUser(tenant, username);
		if (u == null)
			return false;

//		String token = Token.encodeToken(getCrypto(), tenant, username, password);
//		System.out.println(token);

		return password.equals(u.password);
	}

	public boolean isUserAuthenticated(String tenant, String token) throws NoSuchAlgorithmException, UnsupportedEncodingException, DecoderException, EncoderException, TokenException {
		if (StringUtils.isBlank(tenant) || StringUtils.isBlank(token))
			return false;
		
		String toks[] = Token.decodeToken(getCrypto(), token);
		if (toks == null)
			return false;
		if (toks.length != 4)
			return false;
		if (tenant.equals(toks[0]))
			return isUserAuthenticated(toks[0], toks[1], toks[2]);
		return false;
	}

	public String encodeToken(String tenant, String username, String password) throws NoSuchAlgorithmException, TokenException {
		return Token.encodeToken(getCrypto(), tenant, username, password);
	}	
	
	//TODO Check value in token
	public String[] decodeToken(String tenant, String token) throws NoSuchAlgorithmException, TokenException {
		return Token.decodeToken(getCrypto(), token);
	}	
	
	public void checkToken(String token) throws NoSuchAlgorithmException, TokenException {
		Token.decodeToken(getCrypto(), token);
	}
	
	public boolean isFeatureEnabled(String tenant, String username, String feature) {
		UserConf u = getUser(tenant, username);
		if (u == null)
			return false;
		Feature f = u.features.get(feature);
		if (f == null)
			return false;
		long now = System.currentTimeMillis();
		return f.getPeriod().contains(now);
	}

	private Crypto getCrypto() throws NoSuchAlgorithmException {
		return new Crypto(new AESCryptoConfig("KeTQvRIFBlDuJUhslStQAw==", 128, "bRK+VCKvZ+D0qSzSJ9pbEg=="));
	}

	private UserConf getUser(String tenant, String username) {
		UsersConf u = conf.getUsers(tenant);
		if (u != null)
			return u.get(username);
		return null;
	}


}
