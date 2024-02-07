package com.lolmatch.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusChangeDTO {
	
	private final String action = "CHANGE_STATUS";

	private String id;
	
	private StatusType status;
	public enum StatusType {
		ACTIVE,
		INACTIVE
	}
}
