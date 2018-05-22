package org.hierax.myretail.service;

import java.util.Currency;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.model.Product;
import org.hierax.myretail.productdetail.ProductDetailDto;
import org.hierax.myretail.productdetail.ProductDetailService;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${myretail.currency.default}")
	private String defaultCurrencyCode;
	private Currency defaultCurrency;
	
	@PostConstruct
	private void init() {
		defaultCurrency = Currency.getInstance(defaultCurrencyCode);
	}

	/**
	 * Loads product price and details for the given ID
	 * 
	 * @throws ServiceLayerException if anything goes wrong while retrieving data.
	 */
	public Optional<Product> findByProductId(long productId) throws ServiceLayerException {
		Optional<ProductDetailDto> productDetail = productDetailService.findProductDetail(productId);
		if (productDetail.isPresent()) {
			log.info("Found product detail: {}", productDetail);

			Optional<Price> price = priceService.findCurrentPrice(productId, defaultCurrency);
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
	
}
