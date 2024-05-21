package com.lolmatch.chat.util;

import lombok.Data;

@Data
public class ErrorResponse {
	private int status;
	private String error;
	
	public ErrorResponse(String error, int status) {
		this.error = error;
		this.status = status;
		
	}
	
	public static ErrorResponse of(String message, int status) {
		return new ErrorResponse(message, status);
	}
}
