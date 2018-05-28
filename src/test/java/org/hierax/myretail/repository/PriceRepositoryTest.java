package org.hierax.myretail.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:integration-test.properties")
public class PriceRepositoryTest {

	private static final Currency USD = Currency.getInstance("USD");
	private static final Currency EUR = Currency.getInstance("EUR");

	@Autowired
	protected PriceRepository repository;

	@Before
	public void setup() {
		repository.deleteAll();

		repository.save(Price.forSingleCurrency(10, USD, new BigDecimal("15.99")));
		repository.save(Price.forSingleCurrency(10, USD, new BigDecimal("10.99")));
	}

	@Test
	public void save() {
		BigDecimal expectedPrice = new BigDecimal("1.99");
		Price newPrice = repository.save(Price.forSingleCurrency(100, USD, expectedPrice));
		String id = newPrice.getId();

		Optional<Price> retrievedPrice = repository.findById(id);
		assertEquals(expectedPrice, retrievedPrice.get().getPrice(USD).get());
	}

	@Test
	public void save_updateExisting() {
		Price newPrice = repository.save(Price.forSingleCurrency(200, USD, new BigDecimal("1.99")));
		String id = newPrice.getId();

		BigDecimal expectedPrice = new BigDecimal("1.98");
		newPrice.setPrice(USD, expectedPrice);
		Price savedPrice = repository.save(newPrice);
		assertEquals(id, savedPrice.getId());

		Optional<Price> retrievedPrice = repository.findById(id);
		assertEquals(expectedPrice, retrievedPrice.get().getPrice(USD).get());
	}

	@Test
	public void save_addPriceToExisting() {
		long productId = 200;

		BigDecimal usdPrice = new BigDecimal("1.99");
		Price newPrice = repository.save(Price.forSingleCurrency(productId, USD, usdPrice));

		Price retrievedPrice = repository.findByProductId(productId).get();
		assertEquals(newPrice.getId(), retrievedPrice.getId());

		BigDecimal eurPrice = new BigDecimal("1.50");
		retrievedPrice.setPrice(EUR, eurPrice);

		repository.save(retrievedPrice);
		retrievedPrice = repository.findByProductId(productId).get();
		assertEquals(usdPrice, retrievedPrice.getPrice(USD).get());
		assertEquals(eurPrice, retrievedPrice.getPrice(EUR).get());
	}

}
