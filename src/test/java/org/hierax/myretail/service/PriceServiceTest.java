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
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.InvalidMongoDbApiUsageException;

@RunWith(MockitoJUnitRunner.class)
public class PriceServiceTest {

	private static final Currency USD = Currency.getInstance("USD");

	private PriceService service;

	@Mock
	private PriceRepository priceRepository;
	@Spy
	private FakePriceRepository priceRepositorySpy;

	@Before
	public void setup() {
		service = new PriceService(priceRepository);
	}

	@Test
	public void findCurrentPrice_found() throws Exception {
		Price price = new Price(123, LocalDate.parse("2018-01-01"), new BigDecimal(5), USD);

		when(priceRepository.findByProductIdAndCurrency(eq(1234L), any(), eq(USD)))
		.thenReturn(Optional.of(price));

		Optional<Price> actual = service.findCurrentPrice(1234, USD);
		assertEquals(price, actual.get());
	}

	@Test
	public void findCurrentPrice_notFound() throws Exception {
		when(priceRepository.findByProductIdAndCurrency(eq(1234L), any(), eq(USD)))
				.thenReturn(Optional.empty());

		Optional<Price> price = service.findCurrentPrice(1234, USD);
		assertFalse("Price should not be present", price.isPresent());
	}

	@Test(expected=ServiceLayerException.class)
	public void findCurrentPrice_exception() throws Exception {
		when(priceRepository.findByProductIdAndCurrency(eq(1234L), any(), eq(USD)))
		.thenThrow(RepositoryException.class);

		service.findCurrentPrice(1234, USD);
	}
	
	@Test
	public void updatePrice() throws Exception {
		long expectedProductId = 1234;
		BigDecimal expectedPrice = new BigDecimal("1.99");
		Currency expectedCurrency = USD;
		
		PriceService serviceWithSpy = new PriceService(priceRepositorySpy);
		Price actual = serviceWithSpy.updatePrice(expectedProductId, expectedPrice, expectedCurrency);

		assertEquals(expectedProductId, priceRepositorySpy.price.getProductId());
		assertEquals(expectedPrice, priceRepositorySpy.price.getPrice());
		assertEquals(expectedCurrency, priceRepositorySpy.price.getCurrency());
		
		assertEquals(priceRepositorySpy.price, actual);
	}

	@Test(expected=ServiceLayerException.class)
	public void updatePrice_exception() throws Exception {
		when(priceRepository.save(any())).thenThrow(InvalidMongoDbApiUsageException.class);

		service.updatePrice(1234L, new BigDecimal("1.99"), USD);
	}
	
	static abstract class FakePriceRepository implements PriceRepository {
		Price price;		
		@Override
		public <S extends Price> S save(S entity) {
			price = entity;
			return entity;
		}
	}
	
}
