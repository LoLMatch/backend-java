package com.lolmatch.chat.dto;

import com.lolmatch.chat.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ContactDTO {
	
	private User user;
	
	private List<Contact> contacts;
	
	public record Contact (UUID contactId, String username, Integer unreadMessages, String lastMessage, UUID lastMessageSenderId, Boolean isActive, Timestamp lastActiveTimestamp, Timestamp lastMessageTimestamp){}
}


