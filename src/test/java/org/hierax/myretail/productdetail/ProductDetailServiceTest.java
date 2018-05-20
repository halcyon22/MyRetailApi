package org.hierax.myretail.productdetail;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.hierax.myretail.service.ServiceLayerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailServiceTest {

	private ProductDetailService service;

	@Mock
	private RedSkyV2ApiClient redSkyV2ApiClient;

	@Before
	public void setup() {
		service = new ProductDetailService(redSkyV2ApiClient);
	}

	@Test
	public void findProductDetail_notFound() throws Exception {
		when(redSkyV2ApiClient.findProduct(1234L)).thenReturn(Optional.empty());
		
		Optional<ProductDetailDto> productDetail = service.findProductDetail(1234L);
		assertFalse("Product details should be missing", productDetail.isPresent());
	}

	@Test(expected=ServiceLayerException.class)
	public void findProductDetail_apiException() throws Exception {
		when(redSkyV2ApiClient.findProduct(1234L)).thenThrow(ApiClientException.class);
		
		service.findProductDetail(1234L);
	}
	
	@Test
	public void findProductDetail() throws Exception {
		String expectedTitle = "F is for Family";
		ProductDetailDto productDetail = new ProductDetailDto(expectedTitle);
		when(redSkyV2ApiClient.findProduct(1234L)).thenReturn(Optional.of(productDetail));
		
		Optional<ProductDetailDto> actual = service.findProductDetail(1234L);
		assertEquals(expectedTitle, actual.get().getTitle());
	}
	
}
