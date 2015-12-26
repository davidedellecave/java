package ddc.xml3;

/**
 * @author davidedc, 01/Agosto/2013
 */
public class LiteXmlException extends Exception {
	private static final long serialVersionUID = 1L;

	public LiteXmlException(String message) {
		super(message);
	}
	
	public LiteXmlException(Throwable cause) {
		super(cause);
	}

    public LiteXmlException(String message, Throwable cause) {
        super(message, cause);
    }

}
