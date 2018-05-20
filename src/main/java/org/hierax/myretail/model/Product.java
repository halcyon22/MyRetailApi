package org.hierax.myretail.model;

import lombok.Data;

/**
 * Simple retail product.
 */
@Data
public class Product {

	private final long productId;
	private String name;
	private Price currentPrice;
	
}
