package org.hierax.myretail.api.product;

import java.math.BigDecimal;
import java.util.Currency;

import org.hierax.myretail.model.Price;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Price data for API response.
 */
@Data
public class PriceDto {

	@JsonProperty("value")
	private final BigDecimal price;
	private final Currency currency;
	
	public static PriceDto fromPrice(Price price) {
		if (price == null) return null;
		return new PriceDto(price.getPrice(), price.getCurrency());
	}
	
}
