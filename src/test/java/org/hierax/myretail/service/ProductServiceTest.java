package org.hierax.myretail.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.model.Product;
import org.hierax.myretail.productdetail.ProductDetailDto;
import org.hierax.myretail.productdetail.ProductDetailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

	private ProductService service;
	
	@Mock
	private ProductDetailService productDetailService;
	@Mock
	private PriceService priceService;
	
	@Before
	public void setup() {
		service = new ProductService(productDetailService, priceService);
	}

	@Test
	public void findByProductId_noDetails() throws Exception {
		long productId = 1234L;
		
		when(productDetailService.findProductDetail(productId)).thenReturn(Optional.empty());
		
		Optional<Product> product = service.findByProductId(productId);
		assertFalse("Product should be missing", product.isPresent());
	}

	@Test
	public void findByProductId_noPrice() throws Exception {
		long productId = 1234L;
		
		ProductDetailDto productDetail = new ProductDetailDto("Bob's Burgers");
		when(productDetailService.findProductDetail(productId)).thenReturn(Optional.of(productDetail));
		
		when(priceService.findCurrentPrice(productId)).thenReturn(Optional.empty());
		
		Optional<Product> product = service.findByProductId(productId);
		assertEquals("Bob's Burgers", product.get().getName());
		assertNull(product.get().getCurrentPrice());
	}

	@Test(expected=ServiceLayerException.class)
	public void findByProductId_productDetailException() throws Exception {
		long productId = 1234L;
		
		when(productDetailService.findProductDetail(productId)).thenThrow(ServiceLayerException.class);
		
		service.findByProductId(productId);
	}

	@Test(expected=ServiceLayerException.class)
	public void findByProductId_priceException() throws Exception {
		long productId = 1234L;
		
		ProductDetailDto productDetail = new ProductDetailDto("Bob's Burgers");
		when(productDetailService.findProductDetail(productId)).thenReturn(Optional.of(productDetail));
		
		when(priceService.findCurrentPrice(productId)).thenThrow(ServiceLayerException.class);
		
		service.findByProductId(productId);
	}

	@Test
	public void findByProductId() throws Exception {
		long productId = 1234L;
		
		String expectedName = "Bob's Burgers";
		ProductDetailDto productDetail = new ProductDetailDto(expectedName);
		when(productDetailService.findProductDetail(productId)).thenReturn(Optional.of(productDetail));
		
		BigDecimal expectedPrice = new BigDecimal(5);
		Price price = new Price(123, LocalDate.parse("2018-01-01"), expectedPrice, Currency.getInstance("USD"));
		when(priceService.findCurrentPrice(productId)).thenReturn(Optional.of(price));
		
		Optional<Product> product = service.findByProductId(productId);
		assertEquals(expectedName, product.get().getName());
		assertEquals(expectedPrice, product.get().getCurrentPrice().getPrice());
	}
	
}
