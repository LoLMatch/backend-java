package com.lolmatch.service;

import com.lolmatch.dto.FetchMessagesDTO;
import com.lolmatch.dto.IncomingMessageDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
	public FetchMessagesDTO getListOfMessages(UUID senderId, UUID recipientId, Optional<Integer> size) {
		return null;
	}
	
	public void saveMessage(IncomingMessageDTO messageDTO) {
	
	}
	
	public void setMessageRead(IncomingMessageDTO messageDTO) {
	
	}
}
