package org.hierax.myretail.repository;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;

public interface CustomPriceRepository {

	public Optional<Price> findByProductIdAndCurrency(long productId, LocalDate asOfDate, Currency currency);

}
