package com.lolmatch.chat.dto;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Value
@Builder
public class OutgoingMessageDTO {
	
	String action = "MESSAGE";
	UUID id;
	String content;
	Timestamp createdAt;
	// for conversation between users this is proper timestamp
	Timestamp readAt;
	UUID senderId;
	UUID recipientId;
	UUID parentMessageId;
	// this list is for setting read timestamp in group conversation
	List<ReadStatusDTO> readAtList;
}