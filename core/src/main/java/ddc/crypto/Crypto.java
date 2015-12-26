package ddc.crypto;

/**
 * @author davidedc 2014
 *
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ddc.task1.TaskExitCode;
import ddc.util.Base64Utils;

public class Crypto {
	CryptoConfig config = null;

	public Crypto(CryptoConfig config) {
		this.config = config;
	}

	public CryptoResult encrypt(String plainText) {
		CryptoResult result = new CryptoResult();
		try {
			Charset charset = Charset.forName(config.charsetName);
			byte[] input = plainText.getBytes(charset);
			byte[] output = doCipher(input, Cipher.ENCRYPT_MODE);
			result.data = Base64Utils.encodeToString(output, config.charsetName);
			result.setExitCode(TaskExitCode.Succeeded);
		} catch (Exception e) {
			result.setExitCode(TaskExitCode.Failed);
			result.setExitMessage(e.getMessage());
			result.data = null;
			e.printStackTrace();
		}
		return result;
	}

	public CryptoResult decrypt(String base64Encrypted) {
		CryptoResult result = new CryptoResult();
		try {
			byte[] input = Base64Utils.decode(base64Encrypted, config.charsetName);
			byte[] output = doCipher(input, Cipher.DECRYPT_MODE);
			result.data = new String(output, config.charsetName);
			result.setExitCode(TaskExitCode.Succeeded);
		} catch (Exception e) {
			result.setExitCode(TaskExitCode.Failed);
			result.setExitMessage(e.getMessage());
			result.data = null;
			e.printStackTrace();
		}
		return result;
	}

	private byte[] doCipher(byte[] data, int mode) throws Exception {
		byte[] output = new byte[0];
		if (data != null && data.length > 0) {
			byte[] key = Base64Utils.decode(config.base64key, "UTF-8");
			byte[] iv = Base64Utils.decode(config.base64InitVector, "UTF-8");
			Cipher cipher;
			cipher = Cipher.getInstance(config.algorithm + "/" + config.mode);
			IvParameterSpec ivs = new IvParameterSpec(iv);
			SecureRandom secureRandom = SecureRandom.getInstance(config.secureRandomName);
			cipher.init(mode, new SecretKeySpec(key, config.algorithm), ivs, secureRandom);
			output = cipher.doFinal(data);
		}
		return output;
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

	private static CryptoConfig getCryptoConfig() throws NoSuchAlgorithmException{
		CryptoConfig c = new AESCryptoConfig("lwTinBteZXKQoylLHA7UpA==", 128, "Vhfo58z1eTk1LJq3XUTg4A==");
		return c;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, URISyntaxException, IOException {
		int keySize = 256;
		//System.out.println("Encryption:[ size:[" + keySize + "] key:[" + generateBase64Key(keySize) + "]");

		CryptoConfig conf = getCryptoConfig();
		// conf.base64InitVector="GUumJJ+16LzQ1VtmV+MFJw==";
		// conf.base64key="chiavesimmetricaditest==";
//		conf.keyBitSize = 128;
//		conf.mode = "CBC/PKCS5Padding";

		String plainText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><wla><header><timestamp-millis>63496889843288</timestamp-millis><guid>f386a7b3-e7eb-46eb-97ff-f59797ab07f9</guid><context>RetailDigitalLounge</context><session-id></session-id><username>corti</username></header><client><id>I2B</id><return-url></return-url><keepalive-url></keepalive-url><keepalive-period></keepalive-period><keepalive-timeout></keepalive-timeout></client><authorization><ruolo>01</ruolo><codice-compagnia>902</codice-compagnia><codice-fiscale>CRTNDR70S07G999U</codice-fiscale><codice-agenzia>3417</codice-agenzia></authorization></wla>";
		System.out.println("plainText:[" + plainText + "]");
		
		Crypto aes = new Crypto(conf);
		CryptoResult result = aes.encrypt(plainText);
		System.out.println("Encryption:[" + result.toString() + "]");

		result.data="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";
		Crypto aes2 = new Crypto(conf);
		CryptoResult result2 = aes2.decrypt(result.data);

		System.out.println("Decryption:[" + result2.data + "]");

	}
}
