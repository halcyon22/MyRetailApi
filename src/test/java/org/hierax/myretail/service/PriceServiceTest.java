package org.hierax.myretail.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.repository.PriceRepository;
import org.hierax.myretail.repository.RepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PriceServiceTest {

	private PriceService service;

	@Mock
	private PriceRepository priceRepository;

	@Before
	public void setup() {
		service = new PriceService(priceRepository);
	}

	@Test
	public void findCurrentPrice_found() throws Exception {
		Price price = new Price(123, LocalDate.parse("2018-01-01"), new BigDecimal(5), Currency.getInstance("USD"));

		when(priceRepository.findByProductIdAndCurrency(eq(1234L), any(), eq(Currency.getInstance("USD"))))
		.thenReturn(Optional.of(price));

		Optional<Price> actual = service.findCurrentPrice(1234);
		assertEquals(price, actual.get());
	}

	@Test
	public void findCurrentPrice_notFound() throws Exception {
		when(priceRepository.findByProductIdAndCurrency(eq(1234L), any(), eq(Currency.getInstance("USD"))))
				.thenReturn(Optional.empty());

		Optional<Price> price = service.findCurrentPrice(1234);
		assertFalse("Price should not be present", price.isPresent());
	}

	@Test(expected=ServiceLayerException.class)
	public void findCurrentPrice_exception() throws Exception {
		when(priceRepository.findByProductIdAndCurrency(eq(1234L), any(), eq(Currency.getInstance("USD"))))
		.thenThrow(RepositoryException.class);

		service.findCurrentPrice(1234);
	}
	
}
