package org.hierax.myretail.api.product;

import org.hierax.myretail.model.Product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Product data for API response.
 */
@Data @AllArgsConstructor
public class ProductDto {

	// TODO validation
	
	private long id;
	private String name;
	
	@JsonProperty("current_price")
	private PriceDto currentPrice;

	@JsonCreator
	public ProductDto() {}
	
	public static ProductDto fromProduct(Product product) {
		return new ProductDto(product.getProductId(), product.getName(), PriceDto.fromPrice(product.getCurrentPrice()));
	}
}
