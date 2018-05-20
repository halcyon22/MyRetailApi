package org.hierax.myretail.api.product;

import org.hierax.myretail.model.Product;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDto {

	private final long id;
	private final String name;
	
	@JsonProperty("current_price")
	private final PriceDto currentPrice;

	public static ProductDto fromProduct(Product product) {
		return new ProductDto(product.getProductId(), product.getName(), PriceDto.fromPrice(product.getCurrentPrice()));
	}

}
