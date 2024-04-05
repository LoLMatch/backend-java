package com.lolmatch.chat.dto;

import com.lolmatch.chat.util.ActionTypeEnum;

import java.time.Instant;
import java.util.UUID;


public record IncomingMessageDTO(
	ActionTypeEnum type,
	UUID senderId,
	UUID recipientId,
	// if type=='SEND' then this is actual message
	// else if type=='MARK_READ' then might be null because it will not be used
	String content,
	// if type=='SEND' then this is timestamp when message was created
	// else if type=='MARK_READ' then this is timestamp until which all messages have been read
	// if this is empty then server will generate timestamp on its own
	Instant time,
	// if type=='MARK_READ' then this is null
	UUID parentId
) {
}
