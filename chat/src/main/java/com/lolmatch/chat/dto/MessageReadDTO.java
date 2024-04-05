package com.lolmatch.chat.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record MessageReadDTO(
		String action,
		UUID senderId,
		UUID recipientId,
		Timestamp readAt) {
	public MessageReadDTO( UUID senderId, UUID recipientId, Timestamp readAt) {
		this("MESSAGE_READ", senderId,recipientId,readAt);
	}
}
	

