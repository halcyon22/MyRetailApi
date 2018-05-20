package org.hierax.myretail.productdetail;

import lombok.Getter;

public class ApiClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Getter
	private String responseBody;

	public ApiClientException(String responseBody) {
		this.responseBody = responseBody;
	}

	public ApiClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiClientException(Throwable cause) {
		super(cause);
	}

}
