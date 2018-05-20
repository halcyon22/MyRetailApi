package org.hierax.myretail.model;

import lombok.Data;

@Data
public class Product {

	private final long productId;
	private String name;
	private Price currentPrice;
	
}
