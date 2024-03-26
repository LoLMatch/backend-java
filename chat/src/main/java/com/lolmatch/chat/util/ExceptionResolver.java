package com.lolmatch.chat.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionResolver {
	
	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponse> accessDeniedExceptionResolver(AccessDeniedException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getMessage(), 403), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> entityNotFoundExceptionResolver(EntityNotFoundException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
	}
}
