package org.hierax.myretail.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * Simple retail product price.
 */
@Data
public class Price {

	@Id
	private String id;
	
	private final long productId;
	private LocalDate startDate;
	private BigDecimal price;
	private Currency currency;
	
	public Price(long productId, LocalDate startDate, BigDecimal price, Currency currency) {
		this.productId = productId;
		this.startDate = startDate;
		this.price = price;
		this.currency = currency;
	}
	
}
