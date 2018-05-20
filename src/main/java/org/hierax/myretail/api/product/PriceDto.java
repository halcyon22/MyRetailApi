package org.hierax.myretail.api.product;

import java.math.BigDecimal;
import java.util.Currency;

import org.hierax.myretail.model.Price;

import lombok.Data;

@Data
public class PriceDto {

	private final BigDecimal price;
	private final Currency currency;
	
	public static PriceDto fromPrice(Price price) {
		if (price == null) return null;
		return new PriceDto(price.getPrice(), price.getCurrency());
	}
	
}
