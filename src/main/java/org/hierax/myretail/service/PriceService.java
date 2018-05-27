package org.hierax.myretail.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.repository.PriceRepository;
import org.hierax.myretail.repository.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Provides methods related to product prices.
 */
@Service
@RequiredArgsConstructor
public class PriceService {

	private final PriceRepository priceRepository;

	/**
	 * Loads the current price data for the given product and currency.
	 * 
	 * @throws ServiceLayerException
	 *             if anything goes wrong talking to the repository.
	 */
	public Optional<Price> findCurrentPrice(long productId, Currency currency) throws ServiceLayerException {
		try {
			return priceRepository.findByProductIdAndCurrency(productId, LocalDate.now(), currency);
		} catch (RepositoryException e) {
			throw new ServiceLayerException(e);
		}
	}

	/**
	 * Add or update the price for the given product in the given currency.
	 * 
	 * @return the saved Price
	 * @throws ServiceLayerException
	 *             if anything goes wrong talking to the repository.
	 */
	public Price updatePrice(long productId, BigDecimal price, Currency currency) throws ServiceLayerException {
		try {

			// TODO look it up and save
			Price transientPrice = new Price(productId, LocalDate.now(), price, currency);
			Price savedPrice = priceRepository.save(transientPrice);
			return savedPrice;
		} catch (DataAccessException e) {
			throw new ServiceLayerException(e);
		}
	}

}
