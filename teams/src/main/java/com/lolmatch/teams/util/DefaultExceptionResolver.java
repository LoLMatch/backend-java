package com.lolmatch.teams.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class DefaultExceptionResolver {
	
	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponse> accessDeniedExceptionResolver(AccessDeniedException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getMessage(), 403), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> entityNotFoundExceptionResolver(EntityNotFoundException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getMessage(), 404), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
	}
}
