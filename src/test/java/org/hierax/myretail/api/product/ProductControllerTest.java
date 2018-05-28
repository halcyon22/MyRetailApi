package org.hierax.myretail.api.product;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.hierax.myretail.model.Product;
import org.hierax.myretail.service.ProductService;
import org.hierax.myretail.service.ServiceLayerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	private static final Currency USD = Currency.getInstance("USD");

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser
	public void getProduct_notFound() throws Exception {
		long productId = 1234;

		when(productService.findByProductId(productId)).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/products/" + productId)).andExpect(status().isNotFound())
				.andExpect(content().string(containsString("No resource found with id " + productId)));
	}

	@Test
	@WithMockUser
	public void getProduct_serviceLayerException() throws Exception {
		long productId = 1234;

		when(productService.findByProductId(productId)).thenThrow(ServiceLayerException.class);

		this.mockMvc.perform(get("/products/" + productId)).andExpect(status().isInternalServerError())
				.andExpect(content().string(
						containsString("An internal error has occurred. Check the application logs for details")));
	}

	@Test
	@WithMockUser
	public void getProduct() throws Exception {
		long productId = 1234;

		Product product = createProduct();
		when(productService.findByProductId(productId)).thenReturn(Optional.of(product));

		String detailsString = objectMapper.writeValueAsString(ProductDto.fromProduct(product, USD));

		this.mockMvc.perform(get("/products/" + productId)).andExpect(status().isOk())
				.andExpect(content().string(containsString(detailsString)));
	}

	@Test
	@WithMockUser
	public void updatePrice_noCsrf() throws Exception {
		long productId = 1234;

		this.mockMvc
				.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON).content("irrelevant"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser
	public void updatePrice_notFound() throws Exception {
		long productId = 1234;

		Product product = createProduct();
		String detailsString = objectMapper.writeValueAsString(ProductDto.fromProduct(product, USD));

		this.mockMvc
				.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON).content(detailsString)
						.with(csrf()))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("No resource found with id " + productId)));
	}

	@Test
	@WithMockUser
	public void updatePrice_serviceLayerException() throws Exception {
		long productId = 1234;

		Product product = createProduct();
		String detailsString = objectMapper.writeValueAsString(ProductDto.fromProduct(product, USD));

		when(productService.updatePrice(productId, USD, product.getCurrentPrice().getPrice(USD).get()))
				.thenThrow(ServiceLayerException.class);

		this.mockMvc
				.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON).content(detailsString)
						.with(csrf()))
				.andExpect(status().isInternalServerError()).andExpect(content().string(
						containsString("An internal error has occurred. Check the application logs for details")));
	}

	@Test
	@WithMockUser
	public void updatePrice() throws Exception {
		long productId = 1234;

		Product product = createProduct();
		when(productService.updatePrice(productId, USD, product.getCurrentPrice().getPrice(USD).get()))
				.thenReturn(Optional.of(product));

		String detailsString = objectMapper.writeValueAsString(ProductDto.fromProduct(product, USD));

		this.mockMvc
				.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON).content(detailsString)
						.with(csrf()))
				.andExpect(status().isOk()).andExpect(content().string(containsString(detailsString)));
	}

	private Product createProduct() {
		long productId = 1234;

		Product product = new Product(productId);
		product.setName("Westworld Season 2");
		product.setCurrentPrice(Price.forSingleCurrency(productId, USD, new BigDecimal("7.99")));

		return product;
	}

}
