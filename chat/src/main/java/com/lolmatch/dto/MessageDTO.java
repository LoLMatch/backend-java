package com.lolmatch.dto;

import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@Value
public class MessageDTO implements Serializable {
	UUID id;
	String content;
	Timestamp createdAt;
	Timestamp readAt;
	UUID senderIdId;
	UUID recipientIdId;
	UUID parentMessageIdId;
}