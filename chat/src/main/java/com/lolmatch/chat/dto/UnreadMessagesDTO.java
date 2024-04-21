package com.lolmatch.chat.dto;

import java.util.UUID;

public record UnreadMessagesDTO(long unreadMessagesCount, UUID userId) {
}