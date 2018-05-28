package org.hierax.myretail.api.product;

import java.util.Currency;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.hierax.myretail.api.NotFoundException;
import org.hierax.myretail.model.Product;
import org.hierax.myretail.service.ProductService;
import org.hierax.myretail.service.ServiceLayerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Product-related REST API endpoints.
 */
@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@Value("${myretail.currency.default}")
	private String defaultCurrencyCode;
	private Currency defaultCurrency;
	
	@PostConstruct
	private void init() {
		defaultCurrency = Currency.getInstance(defaultCurrencyCode);
	}

	@RequestMapping(path = "/products/{id}", method = RequestMethod.GET)
	public ProductDto getProduct(
			@PathVariable(name = "id", required = true) long productId)
			throws NotFoundException, ServiceLayerException {
		Optional<Product> product = productService.findByProductId(productId);
		if (product.isPresent()) {
			return ProductDto.fromProduct(product.get(), defaultCurrency);
		} else {
			throw new NotFoundException(Long.toString(productId));
		}
	}

	@RequestMapping(path = "/products/{id}", method = RequestMethod.PUT)
	public ProductDto updatePrice(
			@PathVariable(name = "id", required = true) long productId,
			@RequestBody ProductDto transientProduct)
			throws NotFoundException, ServiceLayerException {
		Optional<Product> product = productService.updatePrice(productId, defaultCurrency,
				transientProduct.getCurrentPrice().getPrice());
		if (product.isPresent()) {
			return ProductDto.fromProduct(product.get(), defaultCurrency);
		} else {
			throw new NotFoundException(Long.toString(productId));
		}
	}
	
}
