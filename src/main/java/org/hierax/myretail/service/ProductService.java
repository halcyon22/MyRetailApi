package org.hierax.myretail.service;

import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.model.Product;
import org.hierax.myretail.productdetail.ProductDetailDto;
import org.hierax.myretail.productdetail.ProductDetailService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

	private final ProductDetailService productDetailService;
	private final PriceService priceService;
	
	public Optional<Product> findByProductId(long productId) throws ServiceLayerException {
		Optional<ProductDetailDto> productDetail = productDetailService.findProductDetail(productId);
		if (productDetail.isPresent()) {
			log.info("Found {}", productDetail);
			
			Product product = new Product(productId);
			product.setName(productDetail.get().getTitle());
			
			Optional<Price> price = priceService.findCurrentPrice(productId);

			log.info("Found {}", price);
			
			product.setCurrentPrice(price.orElse(null));
			
			return Optional.of(product);
		}
		else {
			return Optional.empty();
		}
	}
	
}
