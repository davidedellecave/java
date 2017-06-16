package ddc.commons.crypto;

/**
 * @author davidedc 2014
 *
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

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
//			Charset charset = Charset.forName(config.charsetName);
			byte[] input = plainText.getBytes(config.charsetName);
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
	
	public CryptoResult encryptBase32(String plainText) {
		CryptoResult result = new CryptoResult();
		try {
//			Charset charset = Charset.forName(config.charsetName);
			byte[] input = plainText.getBytes(config.charsetName);
			byte[] output = doCipher(input, Cipher.ENCRYPT_MODE);
			Base32 b32 = new Base32();
			result.data = new String(b32.encode(output), config.charsetName);
			result.setExitCode(TaskExitCode.Succeeded);
		} catch (Exception e) {
			result.setExitCode(TaskExitCode.Failed);
			result.setExitMessage(e.getMessage());
			result.data = null;
			e.printStackTrace();
		}
		return result;
	}
	
	public CryptoResult decryptBase32(String base32Encrypted) {
		CryptoResult result = new CryptoResult();
		try {
//			Charset charset = Charset.forName(config.charsetName);
			Base32 b32 = new Base32();
			byte[] input = b32.decode(base32Encrypted.getBytes(config.charsetName));
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
	
	public CryptoResult encryptHex(String plainText) {
		CryptoResult result = new CryptoResult();
		try {
//			Charset charset = Charset.forName(config.charsetName);
			byte[] input = plainText.getBytes(config.charsetName);
			byte[] output = doCipher(input, Cipher.ENCRYPT_MODE);
			result.data = new String(Hex.encodeHex(output));
			result.setExitCode(TaskExitCode.Succeeded);
		} catch (Exception e) {
			result.setExitCode(TaskExitCode.Failed);
			result.setExitMessage(e.getMessage());
			result.data = null;
			e.printStackTrace();
		}
		return result;
	}
	
	public CryptoResult decryptHex(String hexEncrypted) {
		CryptoResult result = new CryptoResult();
		try {
//			Charset charset = Charset.forName(config.charsetName);
			byte[] input = Hex.decodeHex(hexEncrypted.toCharArray());			
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

	public static void main(String[] args) throws NoSuchAlgorithmException, URISyntaxException, IOException, DecoderException {
		int keySize = 128;
		System.out.println("Encryption:[ size:[" + keySize + "] key:[" + generateBase64Key(keySize) + "]");

		CryptoConfig conf = getCryptoConfig();
		// conf.base64InitVector="GUumJJ+16LzQ1VtmV+MFJw==";
		// conf.base64key="chiavesimmetricaditest==";
//		conf.keyBitSize = 128;
//		conf.mode = "CBC/PKCS5Padding";

		String plainText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><wla><header><timestamp-millis>63496889843288</timestamp-millis><guid>f386a7b3-e7eb-46eb-97ff-f59797ab07f9</guid><context>RetailDigitalLounge</context><session-id></session-id><username>corti</username></header><client><id>I2B</id><return-url></return-url><keepalive-url></keepalive-url><keepalive-period></keepalive-period><keepalive-timeout></keepalive-timeout></client><authorization><ruolo>01</ruolo><codice-compagnia>902</codice-compagnia><codice-fiscale>CRTNDR70S07G999U</codice-fiscale><codice-agenzia>3417</codice-agenzia></authorization></wla>";
		System.out.println("plain  :[" + plainText + "]");
		CryptoResult result = null;
		Crypto aes = new Crypto(conf);
		
		//Base64
		result = aes.encrypt(plainText);
		System.out.println("encryptBase64:[" + result.data + "]");
		result = aes.decrypt(result.data);
		System.out.println("decryptBase64:[" + result.data + "]");
			
		//Base32
		result = aes.encryptBase32(plainText);
		System.out.println("encryptBase32:[" + result.data + "]");
		result = aes.decryptBase32(result.data);
		System.out.println("decryptBase32:[" + result.data + "]");
		
		//HEX
		result = aes.encryptHex(plainText);
		System.out.println("encryptHex:[" + result.data + "]");
		result = aes.decryptHex(result.data );
		System.out.println("HexDecrypt:[" + result.data + "]");			
	}
}
