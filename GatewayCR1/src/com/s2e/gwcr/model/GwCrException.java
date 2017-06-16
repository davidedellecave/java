package com.s2e.gwcr.model;

public class GwCrException extends Exception {
	private static final long serialVersionUID = 1L;

	public GwCrException(String message) {
		super(message);
	}

	public GwCrException(Throwable cause) {
		super(cause);
	}

	public GwCrException(String message, Throwable cause) {
		super(message, cause);
	}

}