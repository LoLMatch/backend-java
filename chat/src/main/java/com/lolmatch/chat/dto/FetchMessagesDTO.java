package com.lolmatch.chat.dto;

import com.lolmatch.chat.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchMessagesDTO {
	private List<Message> messages;
	
	private int page;
	private int amount;
	
	private long totalMessages;
}
