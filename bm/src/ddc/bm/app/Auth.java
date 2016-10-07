package ddc.bm.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.StringUtils;

import ddc.bm.conf.Configuration;
import ddc.bm.conf.Feature;
import ddc.bm.conf.User;
import ddc.bm.conf.Users;
import ddc.core.crypto.AESCryptoConfig;
import ddc.core.crypto.Crypto;
import ddc.core.crypto.CryptoResult;

///ToDo transform to singleton
public class Auth {
	// private static final String LOG_INFO = "Auth - ";
	private static Configuration conf = new Configuration();

	public static Auth instance() {
		return new Auth();
	}

	public void recycle() {
		conf.reload();
	}

	public boolean isUserAuthenticated(String tenant, String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, EncoderException {
		if (StringUtils.isBlank(tenant) || StringUtils.isBlank(username) || StringUtils.isBlank(password))
			return false;
		User u = getUser(tenant, username);
		if (u == null)
			return false;

		String token = encodeToken(tenant, username, password);
		System.out.println(token);

		return password.equals(u.password);
	}

	public boolean isUserAuthenticated(String tenant, String token) throws NoSuchAlgorithmException, UnsupportedEncodingException, DecoderException, EncoderException {
		if (StringUtils.isBlank(tenant) || StringUtils.isBlank(token))
			return false;
		String toks[] = decodeToken(token);
		if (toks == null)
			return false;
		if (toks.length != 4)
			return false;
		if (tenant.equals(toks[0]))
			return isUserAuthenticated(toks[0], toks[1], toks[2]);
		return false;
	}

	public boolean isFeatureEnabled(String tenant, String username, String feature) {
		User u = getUser(tenant, username);
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

	private String encodeToken(String... toks) throws NoSuchAlgorithmException {
		String token = "";
		for (String tok : toks) {
			token += tok.trim() + " ";
		}
		token += System.currentTimeMillis();
		CryptoResult result = getCrypto().encryptBase32(token);
		if (result.isFailed())
			return null;
		return result.data;
	}

	private String[] decodeToken(String token) throws NoSuchAlgorithmException {
		CryptoResult result = getCrypto().decryptBase32(token);
		if (result.isFailed())
			return null;
		System.out.println(result.data);
		return result.data.split(" ");
	}
	
	private User getUser(String tenant, String username) {
		Users u = conf.getUsers(tenant);
		if (u != null)
			return u.get(username);
		return null;
	}
}
