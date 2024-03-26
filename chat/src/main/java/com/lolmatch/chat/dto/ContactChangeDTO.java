package com.lolmatch.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactChangeDTO {

	private ActionType action;
	
	private ContactType contactType;
	
	private UUID contactId;
	
	private String contactName;

	public enum ContactType {
		USER,
		GROUP
	}
	
	public enum ActionType {
		CONTACT_ADDED,
		CONTACT_DELETED
	}
}
