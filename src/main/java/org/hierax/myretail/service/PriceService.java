package org.hierax.myretail.service;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.repository.PriceRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceService {
	
	private static final Currency DEFAULT_CURRENCY = Currency.getInstance("USD");

	private final PriceRepository priceRepository;
	
	public Optional<Price> findCurrentPrice(long productId) {
		return priceRepository.findByProductIdAndCurrency(productId, LocalDate.now(), DEFAULT_CURRENCY);
	}
	
}
