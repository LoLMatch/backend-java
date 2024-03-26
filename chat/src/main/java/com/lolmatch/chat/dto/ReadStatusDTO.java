package com.lolmatch.chat.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record ReadStatusDTO(int id, UUID userId, Timestamp readAt, UUID messageId) {
}
