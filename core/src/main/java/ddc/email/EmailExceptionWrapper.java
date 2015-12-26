package ddc.email;

public class EmailExceptionWrapper extends Exception {
	private static final long serialVersionUID = 1L;
	
    public EmailExceptionWrapper(String message) {
        super(message);
    }
    
    public EmailExceptionWrapper(Throwable cause) {
        super(cause);
    }
    
    public EmailExceptionWrapper(String message, Throwable cause) {
        super(message, cause);
    }

}
