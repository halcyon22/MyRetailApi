package org.hierax.myretail.repository;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;

/**
 * Custom price persistence logic.
 */
public interface CustomPriceRepository {

	/**
	 * Loads the effective price data for the given product, as of the given date, in the given currency.
	 */
	public Optional<Price> findByProductIdAndCurrency(long productId, LocalDate asOfDate, Currency currency);

}
