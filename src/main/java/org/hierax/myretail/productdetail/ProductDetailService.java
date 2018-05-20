package org.hierax.myretail.productdetail;

import java.util.Optional;

import org.hierax.myretail.service.ServiceLayerException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

	private final RedSkyV2ApiClient redSkyV2ApiClient; // TODO configure injection via profile
	
	public Optional<ProductDetailDto> findProductDetail(long productId) throws ServiceLayerException {
		try {
			return redSkyV2ApiClient.findProduct(productId);
		} catch (ApiClientException e) {
			throw new ServiceLayerException("Product retrieval failed", e);
		}
	}
	
}
