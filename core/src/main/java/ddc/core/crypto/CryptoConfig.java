package ddc.core.crypto;


/**
 * @author davidedc 2014
 *
 */
public class CryptoConfig {
	public enum CipherMode {};
	
	public String charsetName;
    public String base64key;
    public String base64InitVector;
    //Algorithms:  AES, DES, DESede, Blowfish, IDEA, stream encryption (e.g. RC4), asymmetric encryption (e.g. RSA), and password-based encryption (PBE)
    public String algorithm;
    //Modes: CBC, ECB, OFB, CFB, CTS
    public String mode;
    public int keyBitSize;
    public String secureRandomName;
    
    public CryptoConfig() {
    	algorithm="AES";
    	charsetName="UTF-8";
    	mode="CBC/PKCS5Padding";
    	keyBitSize=128;
    	secureRandomName = "SHA1PRNG";
    }
    
    /*
        // Summary:
        //     The Cipher Block Chaining (CBC) mode introduces feedback. Before each plain
        //     text block is encrypted, it is combined with the cipher text of the previous
        //     block by a bitwise exclusive OR operation. This ensures that even if the
        //     plain text contains many identical blocks, they will each encrypt to a different
        //     cipher text block. The initialization vector is combined with the first plain
        //     text block by a bitwise exclusive OR operation before the block is encrypted.
        //     If a single bit of the cipher text block is mangled, the corresponding plain
        //     text block will also be mangled. In addition, a bit in the subsequent block,
        //     in the same position as the original mangled bit, will be mangled.
        CBC
        //
        // Summary:
        //     The Electronic Codebook (ECB) mode encrypts each block individually. This
        //     means that any blocks of plain text that are identical and are in the same
        //     message, or in a different message encrypted with the same key, will be transformed
        //     into identical cipher text blocks. If the plain text to be encrypted contains
        //     substantial repetition, it is feasible for the cipher text to be broken one
        //     block at a time. Also, it is possible for an active adversary to substitute
        //     and exchange individual blocks without detection. If a single bit of the
        //     cipher text block is mangled, the entire corresponding plain text block will
        //     also be mangled.
        ECB
        //
        // Summary:
        //     The Output Feedback (OFB) mode processes small increments of plain text into
        //     cipher text instead of processing an entire block at a time. This mode is
        //     similar to CFB; the only difference between the two modes is the way that
        //     the shift register is filled. If a bit in the cipher text is mangled, the
        //     corresponding bit of plain text will be mangled. However, if there are extra
        //     or missing bits from the cipher text, the plain text will be mangled from
        //     that point on.
        OFB
        //
        // Summary:
        //     The Cipher Feedback (CFB) mode processes small increments of plain text into
        //     cipher text, instead of processing an entire block at a time. This mode uses
        //     a shift register that is one block in length and is divided into sections.
        //     For example, if the block size is eight bytes, with one byte processed at
        //     a time, the shift register is divided into eight sections. If a bit in the
        //     cipher text is mangled, one plain text bit is mangled and the shift register
        //     is corrupted. This results in the next several plain text increments being
        //     mangled until the bad bit is shifted out of the shift register.
        CFB
        //
        // Summary:
        //     The Cipher Text Stealing (CTS) mode handles any length of plain text and
        //     produces cipher text whose length matches the plain text length. This mode
        //     behaves like the CBC mode for all but the last two blocks of the plain text.
        CTS
     */

}
