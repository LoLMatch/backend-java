package com.lolmatch.chat.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record ContactDTO(
		UUID contactId,
		String username,
		String contactType,
		Long unreadMessages,
		String lastMessage,
		UUID lastMessageSenderId,
		Boolean isActive,
		Timestamp lastActiveTimestamp,
		Timestamp lastMessageTimestamp,
		int profilePictureId
) {
}
