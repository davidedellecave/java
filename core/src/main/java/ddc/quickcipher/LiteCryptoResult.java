package ddc.quickcipher;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import com.s2e.util.Base64Utils;

public class LiteCryptoResult {
	public String base64Data;
    public boolean hasError = false;
    public String error;    
    
    /**
     * Convert to plain text using the charset encoding on config object
     * If base64Data is null or empty return empty string
     * @param conf
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String getPlainTextData(LiteCryptoConfig conf) throws UnsupportedEncodingException {
    	if (base64Data!=null && base64Data.length()>0) {
			Charset charset =Charset.forName(conf.charsetName);
			byte[] plainChar = Base64Utils.decode(base64Data, conf.charsetName);
			return new String(plainChar, charset);
    	}
    	return "";
    }
    
    @Override
    public String toString() {
    	StringBuffer buff = new StringBuffer(256);    
    	buff.append(" base64Data:[").append(base64Data).append("]");
    	if (hasError) {
    		buff.append(" hasError:[").append(hasError).append("]");
    		buff.append(" error:[").append(error).append("]");
    	}
    	return buff.toString();
    }
}
