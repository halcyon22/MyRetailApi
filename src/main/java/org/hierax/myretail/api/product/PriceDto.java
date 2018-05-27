package org.hierax.myretail.api.product;

import java.math.BigDecimal;
import java.util.Currency;

import org.hierax.myretail.model.Price;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Price data for API response.
 */
@Data @AllArgsConstructor
public class PriceDto {

	// TODO validation
	
	@JsonProperty("value")
	private BigDecimal price;
	@JsonProperty("currency_code")
	private Currency currency;
	
	@JsonCreator
	public PriceDto() {} 
	
	public static PriceDto fromPrice(Price price) {
		if (price == null) return null;
		return new PriceDto(price.getPrice(), price.getCurrency());
	}
	
}
