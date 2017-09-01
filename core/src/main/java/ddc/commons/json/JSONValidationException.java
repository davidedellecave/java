package ddc.commons.json;

public class JSONValidationException extends Exception {
	private static final long serialVersionUID = 1L;
	
    public JSONValidationException(String message) {
        super(message);
    }
    
    public JSONValidationException(Throwable cause) {
        super(cause);
    }
    
    public JSONValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
