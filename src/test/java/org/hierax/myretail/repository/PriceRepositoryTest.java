package org.hierax.myretail.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
	private static final Currency JPY = Currency.getInstance("JPY");
	
	@Autowired 
	protected PriceRepository repository;
	
	@Before
	public void setup() {
		repository.deleteAll();

		repository.save(
				new Price(10, LocalDate.parse("1900-01-01"), new BigDecimal("15.99"), USD));
		repository.save(
				new Price(10, LocalDate.parse("2018-01-01"), new BigDecimal("12.99"), USD));
		repository.save(
				new Price(10, LocalDate.parse("2019-01-01"), new BigDecimal("15.99"), USD));

		repository.save(
				new Price(10, LocalDate.parse("1900-01-01"), new BigDecimal("10.99"), EUR));
	}

	@Test
	public void findCurrentByProductId() {
		Optional<Price> price = repository.findByProductIdAndCurrency(10, LocalDate.parse("2018-02-01"), USD);
		assertEquals(new BigDecimal("12.99"), price.get().getPrice());
		assertEquals("2018-01-01", price.get().getStartDate().toString());
	}

	@Test
	public void findCurrentByProductId_beforeAll() {
		Optional<Price> price = repository.findByProductIdAndCurrency(10, LocalDate.parse("1899-02-01"), USD);
		assertFalse(String.format("Price should be empty but was %s", price.toString()), price.isPresent());
	}

	@Test
	public void findCurrentByProductId_afterAll() {
		Optional<Price> price = repository.findByProductIdAndCurrency(10, LocalDate.parse("2100-02-01"), USD);
		assertEquals(new BigDecimal("15.99"), price.get().getPrice());
		assertEquals("2019-01-01", price.get().getStartDate().toString());
	}

	@Test
	public void findCurrentByProductId_euro() {
		Optional<Price> price = repository.findByProductIdAndCurrency(10, LocalDate.parse("2018-02-01"), EUR);
		assertEquals(new BigDecimal("10.99"), price.get().getPrice());
		assertEquals("1900-01-01", price.get().getStartDate().toString());
	}

	@Test
	public void findCurrentByProductId_yen() {
		Optional<Price> price = repository.findByProductIdAndCurrency(10, LocalDate.parse("2018-02-01"), JPY);
		assertFalse(String.format("Price should be empty but was %s", price.toString()), price.isPresent());
	}

	@Test
	public void findCurrentByProductId_noMatchingId() {
		Optional<Price> price = repository.findByProductIdAndCurrency(20, LocalDate.parse("2018-02-01"), USD);
		assertFalse(String.format("Price should be empty but was %s", price.toString()), price.isPresent());
	}

	@Test
	public void save() {
		BigDecimal expectedPrice = new BigDecimal("1.99");
		Price newPrice = repository.save(
				new Price(100, LocalDate.parse("2000-01-01"), expectedPrice, USD));
		String id = newPrice.getId();
		
		Optional<Price> retrievedPrice = repository.findById(id);
		assertEquals(expectedPrice, retrievedPrice.get().getPrice());
	}

	@Test
	public void save_updateExisting() {
		Price newPrice = repository.save(
				new Price(200, LocalDate.parse("2000-01-01"), new BigDecimal("1.99"), USD));
		String id = newPrice.getId();
		 
		BigDecimal expectedPrice = new BigDecimal("1.98");
		newPrice.setPrice(expectedPrice);
		Price savedPrice = repository.save(newPrice);
		assertEquals(id, savedPrice.getId());
		
		Optional<Price> retrievedPrice = repository.findById(id);
		assertEquals(expectedPrice, retrievedPrice.get().getPrice());
	}
	
}
