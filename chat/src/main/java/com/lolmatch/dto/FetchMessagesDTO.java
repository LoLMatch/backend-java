package com.lolmatch.dto;

import com.lolmatch.entity.Message;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FetchMessagesDTO {
	private List<Message> messages;
}
