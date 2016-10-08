package ddc.core.crypto;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import ddc.util.Timespan;

public class Token {
	public static Timespan validity = new Timespan(5, TimeUnit.DAYS);
	
	public static String encodeToken(Crypto crypto, String... toks) throws NoSuchAlgorithmException, TokenException {
		String token = "";
		for (String tok : toks) {
			token += tok.trim() + " ";
		}
		token += System.currentTimeMillis();
		CryptoResult result = crypto.encryptBase32(token);
		if (result.isFailed())
			throw new TokenException("Token - Cannot encode token - " + result.getExitMessage());
		return result.data;
	}

	public static String[] decodeToken(Crypto crypto, String token) throws NoSuchAlgorithmException, TokenException {
		CryptoResult result = crypto.decryptBase32(token);
		if (result.isFailed())
			throw new TokenException("Token - Cannot decode token - " + result.getExitMessage() + " token:[" + token + "]");
//		System.out.println(result.data);
		String[] toks = result.data.split(" ");
		long millis = Long.valueOf(toks[toks.length-1]);		  
		DateTime dt = new DateTime(millis);
		if (dt.plus(validity.getMillis()).isBeforeNow())		
			throw new TokenException("Token - token expired");
		
		return toks;
	}
}
