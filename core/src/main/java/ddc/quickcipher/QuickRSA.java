package ddc.quickcipher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class QuickRSA {
	
	private Charset charset = Charset.forName("UTF-8");

	public QuickRSA() {}
	
	public QuickRSA(Charset charset) {
		this.charset=charset;
	}
	
	public KeyPair generateKeys() {
		try {
			KeyPairGenerator kpg;
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(512);
			KeyPair kp = kpg.genKeyPair();
			return kp;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Encrypt plaintext using Public Key
	 * @param plainText
	 * @param publicKey
	 * @return base64 of encryption using charset set at construction time (default UTF-8)
	 */
	public String encrypt(String plainText, PublicKey publicKey)  {			
		byte[] plainEncrypted = doEncrypt(plainText.getBytes(charset), publicKey);
		byte[] base64Encrypted =Base64.encodeBase64(plainEncrypted); 
		return new String(base64Encrypted, charset);
	}
	
	/**
	 * Decrypt base 64 text using private key
	 * @param base64Encrypted
	 * @param privateKey
	 * @return plain text using charset set at construction time (default UTF-8)
	 */
	public String decrypt(String base64Encrypted, PrivateKey privateKey)  {		
		byte[] plainEncrypted = Base64.decodeBase64(base64Encrypted.getBytes(charset));
		byte[] plain = doDecrypt(plainEncrypted, privateKey);
		return new String(plain, charset);
	}

	
	private byte[] doDecrypt(byte[] data, PrivateKey privateKey) {
		try {
 			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] cipherData = cipher.doFinal(data);
			return cipherData;		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private byte[] doEncrypt(byte[] data, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] cipherData = cipher.doFinal(data);
			return cipherData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, URISyntaxException, IOException {
		QuickRSA rsa = new QuickRSA();
		
		KeyPair keys = rsa.generateKeys();
		
		System.out.println("Public:" + keys.getPublic().toString());
		System.out.println("Private:" + keys.getPrivate().toString());
		
		String s ="Hi guys I'm going to encrypt me :) !";
		System.out.println("Source:" + s);
		
		String e = rsa.encrypt(s, keys.getPublic());
		System.out.println("Encryption:" + e);
		
		String d = rsa.decrypt(e, keys.getPrivate());
		System.out.println("Decryption:" + d);
		
		try {
			//Encryption using stored public key
			byte[] encPublicKey = keys.getPublic().getEncoded();		
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encPublicKey);
			KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory1.generatePublic(publicKeySpec);
			String e2 = rsa.encrypt(s, publicKey);
			System.out.println("Encryption using stored key:" + e2);	
			
			byte[] encPrivateKey = keys.getPrivate().getEncoded();			
			EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(encPrivateKey);
			KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory2.generatePrivate(priKeySpec);
			String d2 = rsa.decrypt(e, privateKey);
			System.out.println("Decryption using stored key:" + d2);			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
// 	public static BigInteger getModulos(PublicKey key) {
// 		KeyFactory fact = KeyFactory.getInstance("RSA");
// 		RSAPublicKeySpec pub = fact.getKeySpec(key, RSAPublicKeySpec.class);
// 		// store modulus and exponent as BigIntegers
// 		return pub.getModulus();
// 		BigInteger exponent = pub.getPublicExponent());
// 	}
//
// 	public static BigInteger getPublicExponet(PrivateKey key) {
// 		KeyFactory fact = KeyFactory.getInstance("RSA");
// 		RSAPublicKeySpec pub = fact.getKeySpec(key, RSAPublicKeySpec.class);
// 		// store modulus and exponent as BigIntegers
// 		return pub.getModulus();
// 		BigInteger exponent = pub.getPublicExponent());
// 	}
//
// 	public static BigInteger getModulos(Key key) {
// 		KeyFactory fact = KeyFactory.getInstance("RSA");
// 		RSAPublicKeySpec pub = fact.getKeySpec(key, RSAPublicKeySpec.class);
// 		// store modulus and exponent as BigIntegers
// 		return pub.getModulus();
// 		BigInteger exponent = pub.getPublicExponent());
// 	}

 //	KeyFactory fact = KeyFactory.getInstance("RSA");
//	RSAPublicKeySpec pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
//	RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,RSAPrivateKeySpec.class);
//
//	// store modulus and exponent as BigIntegers
//	BigInteger modulus = pub.getModulus());
//	BigInteger exponent = pub.getPublicExponent());
//	// ... write to file
//
//	// recreate public key (the same applies to the private key)
//	RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
//	KeyFactory fact = KeyFactory.getInstance("RSA");
//	PublicKey pubKey = fact.generatePublic(keySpec);
}
