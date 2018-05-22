package org.hierax.myretail.api;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.hierax.myretail.service.ServiceLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Customizes the translation of exceptions into an HTTP response.
 */
@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final ZoneId UTC = ZoneId.of("UTC");

	@Autowired
	private ObjectMapper objectMapper;

	@ExceptionHandler(value = { NotFoundException.class })
	protected ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest webRequest) {
		String errorString = makeErrorString(HttpStatus.NOT_FOUND, "No resource found with id " + e.getId());
		return handleExceptionInternal(e, errorString, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
	}

	@ExceptionHandler(value = { ServiceLayerException.class })
	protected ResponseEntity<Object> handleServiceLayerException(ServiceLayerException e, WebRequest request) {
		String errorString = makeErrorString(HttpStatus.INTERNAL_SERVER_ERROR,
				"An internal error has occurred. Check the application logs for details.");
		log.error("Caught ServiceLayerException:", e);
		return handleExceptionInternal(e, errorString, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Object> handleRuntimeException(RuntimeException e, WebRequest request) {
		String errorString = makeErrorString(HttpStatus.INTERNAL_SERVER_ERROR,
				"An internal error has occurred. Check the application logs for details.");
		log.error("Caught RuntimeException:", e);
		return handleExceptionInternal(e, errorString, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	private String makeErrorString(HttpStatus httpStatus, String errorMessage) {
		ErrorDto errorDto = new ErrorDto(OffsetDateTime.now(UTC), httpStatus.value(), httpStatus.getReasonPhrase(),
				errorMessage);
		try {
			return objectMapper.writeValueAsString(errorDto);
		} catch (JsonProcessingException e) {
			log.error("Unabled to serialize: {}", errorDto, e);
			return "";
		}
	}

	@Data
	private static class ErrorDto {
		private final OffsetDateTime timestamp;
		private final int status;
		private final String error;
		private final String message;
	}

}
