package com.lolmatch.chat.dto;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;
import java.util.UUID;


@Value
@Builder
public class OutgoingMessageDTO {
	
	String action = "MESSAGE";
	UUID id;
	String content;
	Timestamp createdAt;
	Timestamp readAt;
	UUID senderId;
	UUID recipientId;
	UUID parentMessageId;
}