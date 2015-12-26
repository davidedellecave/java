package ddc.quickcipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.s2e.util.Base64Utils;

import ddc.util.FileUtils2;

public class QuickCipher {

	private static final String CIPHER_ALGORITHM = "AES";
	private static final String CIPHER_TYPE = "CFB/PKCS5Padding";
	
	private static final String KEY_FILE_NAME = "quick_cipher_key_aes.bin";
	private static final String INITIAL_VECTOR = "ueueueacsdwgfddw";

	protected void generateNewKey(String alg, int size) throws NoSuchAlgorithmException, URISyntaxException, IOException {
		KeyGenerator keyGen = KeyGenerator.getInstance(alg);
		keyGen.init(size);
		SecretKey key = keyGen.generateKey();
		FileOutputStream str = new FileOutputStream(FileUtils2.getLocalFile(QuickCipher.class, "new_quick_cipher_key.bin"));
		str.write(key.getEncoded());
		str.flush();
		str.close();
	}
	
	private byte[] loadKey(File keyFile) throws URISyntaxException, IOException {
		InputStream is = null;
		is = new FileInputStream(keyFile);
		byte[] cipherKey;
		cipherKey = new byte[is.available()];
		is.read(cipherKey);
		is.close();
		return cipherKey;
	}

	protected String showBytes(byte [] data) {
		String result = "";
        for(int i=0; i<data.length; i++)
        {
            result += (data[i]+" ");    
        }
        return result+"\n";
	}
	
	private CipherResult doCipher(String base64text, int mode, String keyFile) {
		try {
			String result = "";
			if (base64text != null) {
				File keyFileObj = keyFile != null ? new File(keyFile) : FileUtils2.getLocalFile(QuickCipher.class, KEY_FILE_NAME);
				byte [] key = loadKey(keyFileObj);
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM+(CIPHER_TYPE.length() > 0 ? ("/"+CIPHER_TYPE) : ""));
				IvParameterSpec ivs = new IvParameterSpec(INITIAL_VECTOR.getBytes());
				cipher.init(mode, new SecretKeySpec(key, CIPHER_ALGORITHM), ivs);
				byte[] input = Base64Utils.decode(base64text, "UTF-8");
				byte[] output = cipher.doFinal(input);
				result = Base64Utils.decodeToString(output, "UTF-8");
			}
			return new CipherResult(result, "");
		} catch (Exception e) {
			e.printStackTrace();
			return new CipherResult("", e.getMessage());
		}
	}
	
	public CipherResult doEncrypt(String plainText, String keyFile) throws UnsupportedEncodingException  {
		CipherResult res = doCipher(Base64Utils.encodeToString(plainText, "UTF-8"), Cipher.ENCRYPT_MODE, keyFile);
		return new CipherResult(res.getData(), res.getError());
	}
	
	public CipherResult encrypt(String plainText, String keyFile) throws UnsupportedEncodingException  {
		return doEncrypt(plainText, keyFile);
	}
	
	public CipherResult encrypt(String plainText) throws UnsupportedEncodingException  {
		return doEncrypt(plainText, null);
	}
	
	public CipherResult doDecrypt(String cipherText, String keyFile) throws UnsupportedEncodingException  {
		CipherResult res = doCipher(cipherText, Cipher.DECRYPT_MODE, keyFile);
		return new CipherResult(Base64Utils.decodeToString(res.getData(), "UTF-8<"), res.getError());
	}
	
	public CipherResult decrypt(String cipherText, String keyFile) throws UnsupportedEncodingException  {
		return doDecrypt(cipherText, keyFile);
	}
	
	public CipherResult decrypt(String cipherText) throws UnsupportedEncodingException  {
		return doDecrypt(cipherText, null);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, URISyntaxException, IOException {
		QuickCipher obj = new QuickCipher();
		String plainText = "u�u�";
		System.out.println("Plaintext: >"+plainText+"<");
		CipherResult cipherRes = obj.encrypt(plainText);
		System.out.println("Cipher result: >" + cipherRes.getData()+"< (" + 
				(cipherRes.hasError()?("error: " + cipherRes.getError()):"no error")
				+")");
		if (!cipherRes.hasError()) {
			cipherRes = obj.decrypt(cipherRes.getData());
			System.out.println("Redecrypted result: >" + cipherRes.getData()+"< (" + 
					(cipherRes.hasError()?("error: " + cipherRes.getError()):"no error")
					+")");
		}
	}
	
}
