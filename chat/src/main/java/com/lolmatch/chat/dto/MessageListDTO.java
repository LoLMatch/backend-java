package com.lolmatch.chat.dto;

import java.util.List;


public record MessageListDTO(List<MessageDTO> messages, int page, int amount, long totalMessages) {

}
