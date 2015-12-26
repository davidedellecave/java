package ddc.crypto;

/**
 * @author davidedc 2014
 *
 */
public class AESCryptoConfig extends CryptoConfig {
	
	public AESCryptoConfig(String privateKey, int publicKeySizeBit, String publicKey) {
		this.base64InitVector = privateKey;
		this.keyBitSize = publicKeySizeBit;
		this.base64key = publicKey;
		this.mode = "CBC/PKCS5Padding";
		this.secureRandomName = "SHA1PRNG";
	}

}
