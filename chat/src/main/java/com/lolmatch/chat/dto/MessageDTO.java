package com.lolmatch.chat.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


public record MessageDTO
		(String action,
		 UUID id,
		 String content,
		 Timestamp createdAt,
		 // for conversation between users this is proper timestamp
		 Timestamp readAt,
		 UUID senderId,
		 UUID recipientId,
		 UUID parentMessageId,
		 // this list is for setting read timestamp in group conversation
		 List<ReadStatusDTO> readAtList) {
	
	public MessageDTO( String action, UUID id, String content,  Timestamp createdAt, Timestamp readAt, UUID senderId, UUID recipientId) {
		this(action, id, content, createdAt, readAt, senderId, recipientId, null, null);
	}
	
	public MessageDTO( String action, UUID id, String content,  Timestamp createdAt, Timestamp readAt, UUID senderId, UUID recipientId, UUID parentMessageId) {
		this(action, id, content, createdAt, readAt, senderId, recipientId, parentMessageId, null);
	}
	
	public MessageDTO( UUID id, String content,  Timestamp createdAt, Timestamp readAt, UUID senderId, UUID recipientId, UUID parentMessageId) {
		this("MESSAGE", id, content, createdAt, readAt, senderId, recipientId, parentMessageId, null);
	}
}