package org.hierax.myretail.productdetail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Getter
	private final String responseBody;

}
