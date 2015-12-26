package ddc.http;

public class HttpClientExceptionWrapper extends Exception {
	private static final long serialVersionUID = 1L;
	
    public HttpClientExceptionWrapper(String message) {
        super(message);
    }
    
    public HttpClientExceptionWrapper(Throwable cause) {
        super(cause);
    }
    
    public HttpClientExceptionWrapper(String message, Throwable cause) {
        super(message, cause);
    }

}
