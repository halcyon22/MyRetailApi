package org.hierax.myretail.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Price {

	@Id
	private String id;
	
	private final long productId;
	private final LocalDate startDate;
	private final BigDecimal price;
	private final Currency currency;
	
}
