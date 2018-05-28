package org.hierax.myretail.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple retail product price.
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Price {

	@Id
	@Getter
	private String id;
	
	@Getter
	private final long productId;
	
	private final Map<Currency, BigDecimal> prices;

	public Optional<BigDecimal> getPrice(Currency currency) {
		Objects.requireNonNull(currency, "Parameter currency cannot be null");
		return Optional.ofNullable(prices.get(currency));
	}
	
	public void setPrice(Currency currency, BigDecimal price) {
		Objects.requireNonNull(currency, "Parameter currency cannot be null");
		prices.put(currency, price);
	}
	
	public static Price forSingleCurrency(long productId, Currency currency, BigDecimal price) {
		Map<Currency, BigDecimal> priceMap = new HashMap<Currency, BigDecimal>(1);
		priceMap.put(currency, price);
		return new Price(productId, priceMap);
	}
	
}
