package ddc.core.http;

public class HttpLiteClientException extends Exception {
	private static final long serialVersionUID = 1L;
	
    public HttpLiteClientException(String message) {
        super(message);
    }
    
    public HttpLiteClientException(Throwable cause) {
        super(cause);
    }
    
    public HttpLiteClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
