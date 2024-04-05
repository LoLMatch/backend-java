package com.lolmatch.chat.dto;

public record UserStatusChangeDTO (
	
	  String action,

	 String id,
	
	 StatusType status
	
	
) {
	public UserStatusChangeDTO(String id, StatusType status) {
		this("CHANGE_STATUS", id, status);
	}
	
	public enum StatusType {
		ACTIVE,
		INACTIVE
	}
}
