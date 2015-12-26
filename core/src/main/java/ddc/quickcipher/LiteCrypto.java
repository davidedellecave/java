package ddc.quickcipher;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ddc.util.Base64Utils;

public class LiteCrypto {
	LiteCryptoConfig config = null;

    public LiteCrypto(LiteCryptoConfig config) {
        this.config=config;
    }
    
	public LiteCryptoResult encrypt(String plainText)  {	
		Charset charset = Charset.forName(config.charsetName);
		return doCipher(plainText.getBytes(charset), Cipher.ENCRYPT_MODE);
	}
	
	public LiteCryptoResult decrypt(String base64Encrypted) throws UnsupportedEncodingException  {				
		return doCipher(base64Encrypted);
	}
	
	private LiteCryptoResult doCipher(String base64Encrypted) throws UnsupportedEncodingException {
		byte[] encText = Base64Utils.decode(base64Encrypted, config.charsetName);
		return doCipher(encText, Cipher.DECRYPT_MODE);				
	}
	
	private LiteCryptoResult doCipher(byte[] text, int mode) {
		LiteCryptoResult result = new LiteCryptoResult();
		result.base64Data = "";
		result.error = "";
		try {
			if (text != null && text.length>0) {
				byte [] key = Base64Utils.decode(config.base64key, "UTF-8");
				byte [] iv = Base64Utils.decode(config.base64InitVector, "UTF-8");				
				Cipher cipher = Cipher.getInstance(config.algorithm + "/" + config.mode);
				IvParameterSpec ivs = new IvParameterSpec(iv);
				cipher.init(mode, new SecretKeySpec(key, config.algorithm), ivs);
				byte[] output = cipher.doFinal(text);				
				result.base64Data = Base64Utils.encodeToString(output, "UTF-8");				
			} 
			result.hasError = false;
		} catch (Exception e) {
			result.hasError = true;
			e.printStackTrace();
			result.base64Data=null;
			result.error = e.getMessage();
		}
		return result;
	}
    
	
	private static String generateBase64Key(int keySize) {
		try {			
			KeyGenerator kgen = KeyGenerator.getInstance("AES", "SunJCE");
			kgen.init(keySize);
			byte[] binKey = kgen.generateKey().getEncoded();
			return Base64Utils.encodeToString(binKey, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return "";
	}
//	public static void main(String[] args) throws NoSuchAlgorithmException, URISyntaxException, IOException {
//		int keySize=256;
//		System.out.println("Encryption:[ size:[" + keySize + "] key:[" + generateBase64Key(keySize) + "]");		
//		
//		LiteCryptoConfig conf = new LiteCryptoConfig();
//		conf.base64InitVector="GUumJJ+16LzQ1VtmV+MFJw==";
//		conf.base64key="chiavesimmetricaditest==";
//		conf.keyBitSize=128;
//		conf.mode="CBC/PKCS5Padding";
//		
//		//String plainText = "Ciao! Ma che bella giornata, piove :(";
//		//String plainText = "Ciao! Ma che parola � �����������$<> !";
//		String plainText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><wla><header><timestamp-millis>63496889843288</timestamp-millis><guid>f386a7b3-e7eb-46eb-97ff-f59797ab07f9</guid><context>RetailDigitalLounge</context><session-id></session-id><username>corti</username></header><client><id>I2B</id><return-url></return-url><keepalive-url></keepalive-url><keepalive-period></keepalive-period><keepalive-timeout></keepalive-timeout></client><authorization><ruolo>01</ruolo><codice-compagnia>902</codice-compagnia><codice-fiscale>CRTNDR70S07G999U</codice-fiscale><codice-agenzia>3417</codice-agenzia></authorization></wla>";
//		
//		LiteCrypto aes = new LiteCrypto(conf);
//		LiteCryptoResult result = aes.encrypt(plainText);		
//		System.out.println("Encryption:[" + result + "]");
//			
//		//result.base64Data ="xMuETXPWpS2qTEWkl22YQFgth01Q6iSz1ZosKQvr4d9iS0cbiPh4oPiUvBRg/bpW9VV86oIlbOhheIqpIg9A+mroKH69/FnSTS8HBf3dwTZpnlxI08wQI5Wh2WzVsBFjMkz6V75+pQLhGMVwc/O/r6Z/mTBBA2ejZpaRtOupNPDw+ijhYJCo7a9xh2KCDKHL6cRZ5onf3Q2a2GKBmqP74fAU6lOxvbD52mPAKkr40B/FupGEsIAQGlQYPMW0E/KIPmSHD4UurpXk0cC+fnx5mZcS/q1tnH6MTjYxW90tLlyhbjl+zLidsZKRMy/7OxrQU7Li6/uQt6MwiKGBPHYhzW1OsXCwH0TIiWlmUVaSmQbjRXQ/k72sXOVg2eONkZaap+UEbfGKP5TwhrjjihSVZpsTMaEcnJV45/jeJ24BtIZS0ELSXe8npU2+ZUq71n/+gVyRaIt/bX2MAU2plZ7mjtQ5Bygp9OReszuT9NAxYv3ye0h7JPdJQEXMKx+POJDSG97tyh2xSW3fr3j2HhDysVfWRb60lB9rE4ULEA1du2rQHBgJLRtivD8BxoEOKiF7zYImkLCTizkCphph5bKB66DqHC/ZV554X9K6FbpZGy2MdFHaHOqytUQ8BveKhctj1H+JKzj64bq89stlwWOhomRUX1uf+YliZg4c/aG5H6GMoR6lT6Gun8GKuleQ2VZS2sWgGycw73vSPt43PV9Mubijp3bOHDs7eb06ldrDzJIWsF85cteXe9SoSVXmpm/gEGkmwmrAeo6tl3O9x4XA7E/VDNBxlk9irEKrdYI23xk="; 
//		
//		LiteCrypto aes2 = new LiteCrypto(conf);
//		LiteCryptoResult result2 = aes2.decrypt(result.base64Data);
//		
//		System.out.println("Decryption:[" + result2.getPlainTextData(conf) + "]");
//				
//	}
}
