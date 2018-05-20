package org.hierax.myretail.service;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.repository.PriceRepository;
import org.hierax.myretail.repository.RepositoryException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Provides methods related to product prices.
 */
@Service
@RequiredArgsConstructor
public class PriceService {
	
	private static final Currency DEFAULT_CURRENCY = Currency.getInstance("USD");

	private final PriceRepository priceRepository;
	
	/**
	 * Loads the current price data in USD for the given product.
	 * 
	 * @throws ServiceLayerException if anything goes wrong talking to the repository.
	 */
	public Optional<Price> findCurrentPrice(long productId) throws ServiceLayerException {
		try {
			return priceRepository.findByProductIdAndCurrency(productId, LocalDate.now(), DEFAULT_CURRENCY);
		} catch (RepositoryException e) {
			throw new ServiceLayerException(e);
		}
	}
	
}
