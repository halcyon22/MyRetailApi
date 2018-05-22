package org.hierax.myretail.api;

import org.hierax.myretail.service.ServiceLayerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Customizes the translation of exceptions into an HTTP response.
 */
@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
        String responseBody = "No resource found with id " + e.getId();
		return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

	@ExceptionHandler(value = { ServiceLayerException.class })
    protected ResponseEntity<Object> handleServiceLayerException(ServiceLayerException e, WebRequest request) {
        String responseBody = "An internal error has occurred. Check the application logs for details.";
        log.error("Caught ServiceLayerException:", e);
		return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

	@ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> handleServiceLayerException(RuntimeException e, WebRequest request) {
        String responseBody = "An internal error has occurred. Check the application logs for details.";
        log.error("Caught RuntimeException:", e);
		return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
