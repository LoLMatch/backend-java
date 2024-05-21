package com.lolmatch.chat.dto;

import java.util.UUID;

public record ContactChangeDTO(ActionType action, ContactType contactType, UUID contactId, String contactName) {
	public enum ContactType {
		USER,
		GROUP
	}
	
	public enum ActionType {
		CONTACT_ADDED,
		CONTACT_DELETED
	}
}
