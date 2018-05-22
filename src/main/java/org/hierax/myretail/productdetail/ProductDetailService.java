package org.hierax.myretail.productdetail;

import java.util.Optional;

import org.hierax.myretail.service.ServiceLayerException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Provides methods related to product details.
 */
@Service
@RequiredArgsConstructor
public class ProductDetailService {

	private final RedSkyV2ApiClient redSkyV2ApiClient;
	
	/**
	 * Loads product details for the given ID from an external system.
	 * 
	 * @throws ServiceLayerException if anything goes wrong talking to the external API.
	 */
	public Optional<ProductDetailDto> findProductDetail(long productId) throws ServiceLayerException {
		try {
			return redSkyV2ApiClient.findProduct(productId);
		} catch (ApiClientException e) {
			throw new ServiceLayerException("Product retrieval failed", e);
		}
	}
	
}
