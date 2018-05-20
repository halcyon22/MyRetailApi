package org.hierax.myretail.productdetail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Customizes handling of failed RestTemplate calls. 
 */
@Component
@Slf4j
public class CustomResponseErrorHandler implements ResponseErrorHandler {

	private static final List<HttpStatus.Series> ERROR_STATUS_SERIES = Arrays.asList(HttpStatus.Series.CLIENT_ERROR,
			HttpStatus.Series.SERVER_ERROR);
	
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return ERROR_STATUS_SERIES.contains(response.getStatusCode().series());
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		String responseBody = streamToString(response.getBody());
		if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new ResourceNotFoundException(responseBody);
		}
		else {
			log.error("API call failed: {} {} : {}", response.getStatusCode(), response.getStatusText(),
					responseBody);
			throw new ApiClientException(responseBody);
		}
	}

	private String streamToString(InputStream inputStream) {
		String text = "";
	    try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
	    	scanner.useDelimiter("\\A");
	    	if (scanner.hasNext()) {
	    		text = scanner.next();
	    	}
	    }
	    return text;
	}
	
}
