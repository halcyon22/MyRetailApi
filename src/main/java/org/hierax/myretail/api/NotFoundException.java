package org.hierax.myretail.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	@Getter
	private final String id;

}
