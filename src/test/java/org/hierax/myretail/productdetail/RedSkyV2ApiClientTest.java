package org.hierax.myretail.productdetail;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@RestClientTest(value = RedSkyV2ApiClient.class, 
	includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomResponseErrorHandler.class))
public class RedSkyV2ApiClientTest {

	@Autowired
	private RedSkyV2ApiClient client;

	@Autowired
	private MockRestServiceServer server;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void findProduct_notFound() throws Exception {
		server.expect(requestTo("/pdp/tcin/1234?"
				+ "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
				+ "rating_and_review_statistics,question_answer_statistics"))
		.andRespond(withStatus(HttpStatus.NOT_FOUND));
		
		Optional<ProductDetailDto> productDetails = client.findProduct(1234);
		assertFalse("Product details should be missing", productDetails.isPresent());
	}

	@Test(expected=ApiClientException.class)
	public void findProduct_serverError() throws Exception {
		server.expect(requestTo("/pdp/tcin/1234?"
				+ "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
				+ "rating_and_review_statistics,question_answer_statistics"))
		.andRespond(withServerError());
		
		client.findProduct(1234);
	}

	@Test(expected=ApiClientException.class)
	public void findProduct_badRequest() throws Exception {
		server.expect(requestTo("/pdp/tcin/1234?"
				+ "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
				+ "rating_and_review_statistics,question_answer_statistics"))
		.andRespond(withBadRequest());
		
		client.findProduct(1234);
	}

	@Test
	public void findProduct_successfulNullResponse() throws Exception {
		server.expect(requestTo("/pdp/tcin/1234?"
				+ "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
				+ "rating_and_review_statistics,question_answer_statistics"))
		.andRespond(withSuccess());
		
		Optional<ProductDetailDto> productDetails = client.findProduct(1234);

		assertFalse(productDetails.isPresent());
	}

	@Test(expected=ApiClientException.class)
	public void findProduct_timeout() throws Exception {
		server.expect(requestTo("/pdp/tcin/1234?"
				+ "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
				+ "rating_and_review_statistics,question_answer_statistics"))
		.andRespond(TimeoutResponseCreator.withTimeout());
		
		client.findProduct(1234);
	}

	@Test
	public void findProduct() throws Exception {
		String detailsString = objectMapper.writeValueAsString(new ProductDetailDto("Westworld Season 2"));

		server.expect(requestTo("/pdp/tcin/1234?"
				+ "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
				+ "rating_and_review_statistics,question_answer_statistics"))
		.andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
		
		Optional<ProductDetailDto> productDetails = client.findProduct(1234);

		assertEquals("Westworld Season 2", productDetails.get().getTitle());
	}

	private static class TimeoutResponseCreator implements ResponseCreator {

		@Override
		public ClientHttpResponse createResponse(ClientHttpRequest request) throws IOException {
			throw new SocketTimeoutException();
		}
		
		public static TimeoutResponseCreator withTimeout() {
			return new TimeoutResponseCreator();
		}
		
	}
	
}
