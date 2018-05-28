package org.hierax.myretail.service;

import java.math.BigDecimal;
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
	 * Loads the current price data for the given product.
	 * 
	 * @throws ServiceLayerException
	 *             if anything goes wrong talking to the repository.
	 */
	public Optional<Price> findCurrentPrice(long productId) throws ServiceLayerException {
		try {
			return priceRepository.findByProductId(productId);
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
	public Price updatePrice(long productId, Currency currency, BigDecimal price) throws ServiceLayerException {
		try {
			Optional<Price> existingPrice = priceRepository.findByProductId(productId);
			Price toSave = existingPrice.orElse(Price.forSingleCurrency(productId, currency, price));
			toSave.setPrice(currency, price);
			
			Price savedPrice = priceRepository.save(toSave);
			return savedPrice;
		} catch (DataAccessException e) {
			throw new ServiceLayerException(e);
		}
	}

}
