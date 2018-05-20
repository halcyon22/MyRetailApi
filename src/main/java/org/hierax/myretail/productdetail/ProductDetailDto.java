package org.hierax.myretail.productdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product detail data from an external API.
 */
@Data
@NoArgsConstructor
public class ProductDetailDto {

	private Product product;
	
	public ProductDetailDto(String title) {
		this.product = new Product(title);
	}
	
	@JsonIgnore
	public String getTitle() {
		return product.item.productDescription.title;
	}

	@Data
	@NoArgsConstructor
	private static class Product {
		private Item item;
		
		private Product(String title) {
			this.item = new Item(title);
		}
	}
	
	@Data
	@NoArgsConstructor
	private static class Item {
		@JsonProperty("product_description")
		private ProductDescription productDescription;
		
		private Item(String title) {
			this.productDescription = new ProductDescription(title);
		}
	}
	
	@Data
	@NoArgsConstructor
	private static class ProductDescription {
		private String title;
		
		private ProductDescription(String title) {
			this.title = title;
		}
	}
	
}
