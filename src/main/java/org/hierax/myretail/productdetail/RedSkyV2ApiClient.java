package org.hierax.myretail.productdetail;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

/**
 * Client for the RedSky V2 API.
 */
@Service
@RequiredArgsConstructor
class RedSkyV2ApiClient {

	private static final String PATH_TEMPLATE = "/pdp/tcin/{productId}"
			+ "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
			+ "rating_and_review_statistics,question_answer_statistics";
	
	private final RestTemplateBuilder restTemplateBuilder;
	private final CustomResponseErrorHandler errorHandler;
	private RestTemplate restTemplate;

	@Value("${redsky.v2.uri}")
	private String baseUrl;

	@Value("${redsky.v2.connect.timeout.ms}")
	private String connectTimeoutMs;

	@Value("${redsky.v2.read.timeout.ms}")
	private String readTimeoutMs;
	
	@PostConstruct
	private void init() {
		this.restTemplate = restTemplateBuilder
				.errorHandler(errorHandler)
				.rootUri(baseUrl)
				.setConnectTimeout(Integer.parseInt(connectTimeoutMs))
				.setReadTimeout(Integer.parseInt(readTimeoutMs))
				.build();
	}
	
	/**
	 * Calls RedSky for product details for the given ID.
	 * @throws ApiClientException if anything goes wrong talking to RedSky.
	 */
	Optional<ProductDetailDto> findProduct(long productId) {
		ResponseEntity<ProductDetailDto> responseEntity;
		try {
			responseEntity = restTemplate.getForEntity(PATH_TEMPLATE, ProductDetailDto.class, productId);
		} catch (ResourceNotFoundException e) {
			return Optional.empty();
		} catch (ResourceAccessException e) {
			throw new ApiClientException(e);
		}
		
		ProductDetailDto productDetail = responseEntity.getBody();
		return Optional.ofNullable(productDetail);
	}
	
}
