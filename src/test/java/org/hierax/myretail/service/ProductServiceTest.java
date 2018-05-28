package org.hierax.myretail.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

	private static final Currency USD = Currency.getInstance("USD");

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
		Price price = Price.forSingleCurrency(123, USD, expectedPrice);
		when(priceService.findCurrentPrice(productId)).thenReturn(Optional.of(price));
		
		Optional<Product> product = service.findByProductId(productId);
		assertEquals(expectedName, product.get().getName());
		assertEquals(expectedPrice, product.get().getCurrentPrice().getPrice(USD).get());
	}
	
	@Test
	public void updatePrice_noDetails() throws Exception {
		long productId = 1234L;
		
		when(productDetailService.findProductDetail(productId)).thenReturn(Optional.empty());
		
		Optional<Product> product = service.updatePrice(productId, USD, new BigDecimal("1.99"));
		assertFalse("Product should be missing", product.isPresent());
	}

	@Test(expected=ServiceLayerException.class)
	public void updatePrice_productDetailException() throws Exception {
		long productId = 1234L;
		
		when(productDetailService.findProductDetail(productId)).thenThrow(ServiceLayerException.class);
		
		service.findByProductId(productId);
	}

	@Test(expected=ServiceLayerException.class)
	public void updatePrice_priceException() throws Exception {
		long productId = 1234L;
		BigDecimal price = new BigDecimal("1.99");
		
		ProductDetailDto productDetail = new ProductDetailDto("Bob's Burgers");
		when(productDetailService.findProductDetail(productId)).thenReturn(Optional.of(productDetail));
		
		when(priceService.updatePrice(productId, USD, price)).thenThrow(ServiceLayerException.class);
		
		service.updatePrice(productId, USD, price);
	}

	@Test
	public void updatePrice() throws Exception {
		long productId = 1234L;
		
		String expectedName = "Bob's Burgers";
		ProductDetailDto productDetail = new ProductDetailDto(expectedName);
		when(productDetailService.findProductDetail(productId)).thenReturn(Optional.of(productDetail));
		
		BigDecimal expectedPrice = new BigDecimal("1.99");
		Currency expectedCurrency = USD;
		Price price = Price.forSingleCurrency(productId, expectedCurrency, expectedPrice);
		when(priceService.updatePrice(productId, expectedCurrency, expectedPrice)).thenReturn(price);		

		Optional<Product> product = service.updatePrice(productId, expectedCurrency, expectedPrice);
		assertEquals(expectedName, product.get().getName());
		assertEquals(expectedPrice, product.get().getCurrentPrice().getPrice(expectedCurrency).get());
	}
	
}
