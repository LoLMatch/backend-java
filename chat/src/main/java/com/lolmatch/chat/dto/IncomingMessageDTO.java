package com.lolmatch.chat.dto;

import com.lolmatch.chat.util.ActionTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class IncomingMessageDTO {
	
	private ActionTypeEnum type;
	
	private UUID senderId;
	
	private UUID recipientId;
	
	// if type=='SEND' then this is actual message
	// else if type=='MARK_READ' then might be null because it will not be used
	private String content;
	
	// if type=='SEND' then this is timestamp when message was created
	// else if type=='MARK_READ' then this is timestamp until which all messages have been read
	// if this is empty then server will generate timestamp on its own
	private Instant time;
	
	// if type=='MARK_READ' then this is null
	private UUID parentId;
}
