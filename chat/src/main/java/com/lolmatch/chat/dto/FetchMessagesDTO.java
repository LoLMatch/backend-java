package com.lolmatch.chat.dto;

import com.lolmatch.chat.entity.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FetchMessagesDTO {
	private List<Message> messages;
}
