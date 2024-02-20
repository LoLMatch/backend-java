package com.lolmatch.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class MessageReadDTO {
	
	String action = "MESSAGE_READ";
	UUID senderId;
	UUID recipientId;
	Timestamp readAt;
	
}
