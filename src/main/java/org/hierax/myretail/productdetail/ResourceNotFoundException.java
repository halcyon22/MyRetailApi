package org.hierax.myretail.productdetail;

public class ResourceNotFoundException extends ApiClientException {
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String responseBody) {
		super(responseBody);
	}
	
}
