package org.hierax.myretail.service;

public class ServiceLayerException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServiceLayerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceLayerException(String message) {
		super(message);
	}

	public ServiceLayerException(Throwable cause) {
		super(cause);
	}

}
