package org.hierax.myretail.productdetail;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@RestClientTest(value = RedSkyV2ApiClient.class, includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomResponseErrorHandler.class))
public class RedSkyV2ApiClientTest {

	@Autowired
	private RedSkyV2ApiClient client;

	@Autowired
	private MockRestServiceServer server;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setUp() throws Exception {
		String detailsString = objectMapper.writeValueAsString(new ProductDetailDto("Westworld Season 2"));

		server.expect(requestTo("/pdp/tcin/1234?"
				+ "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,"
				+ "rating_and_review_statistics,question_answer_statistics"))
		.andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
	}

	@Test
	public void whenCallingGetUserDetails_thenClientMakesCorrectCall() throws Exception {
		Optional<ProductDetailDto> productDetails = this.client.findProduct(1234);

		assertEquals("Westworld Season 2", productDetails.get().getTitle());
	}

}
