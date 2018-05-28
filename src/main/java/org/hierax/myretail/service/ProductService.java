package org.hierax.myretail.service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.model.Product;
import org.hierax.myretail.productdetail.ProductDetailDto;
import org.hierax.myretail.productdetail.ProductDetailService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides methods related to products.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductDetailService productDetailService;
	private final PriceService priceService;
	
	/**
	 * Loads product price and details for the given ID
	 * 
	 * @throws ServiceLayerException if anything goes wrong while retrieving data.
	 */
	public Optional<Product> findByProductId(long productId) throws ServiceLayerException {
		Optional<ProductDetailDto> productDetail = productDetailService.findProductDetail(productId);
		if (productDetail.isPresent()) {
			log.info("Found product detail: {}", productDetail);

			Optional<Price> price = priceService.findCurrentPrice(productId);
			log.info("Found price: {}", price);
			
			Product product = new Product(productId);
			product.setName(productDetail.get().getTitle());
			product.setCurrentPrice(price.orElse(null));
			
			return Optional.of(product);
		}
		else {
			return Optional.empty();
		}
	}
	
	/**
	 * Add or update the price for the given product in the given currency.
	 * 
	 * @return
	 * @throws ServiceLayerException
	 */
	public Optional<Product> updatePrice(long productId, Currency currency, BigDecimal price)
			throws ServiceLayerException {
		Optional<ProductDetailDto> productDetail = productDetailService.findProductDetail(productId);
		if (productDetail.isPresent()) {
			log.info("Found product detail: {}", productDetail);

			Price savedPrice = priceService.updatePrice(productId, currency, price);
			
			Product product = new Product(productId);
			product.setName(productDetail.get().getTitle());
			product.setCurrentPrice(savedPrice);
			
			return Optional.of(product);
		}
		else {
			return Optional.empty();
		}
	}
	
}
