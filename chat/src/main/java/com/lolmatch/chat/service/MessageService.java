package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dto.FetchMessagesDTO;
import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	
	private final UserService userService;
	
	public FetchMessagesDTO getListOfMessages(UUID senderId, UUID recipientId, Optional<Integer> size) {
		return null;
	}
	
	public MessageDTO saveMessage(IncomingMessageDTO incomingMessage) {
		Message message = new Message();
		message.setSender(userService.getUserByUUID(incomingMessage.getSenderId()));
		message.setRecipient(userService.getUserByUUID(incomingMessage.getRecipientId()));
		if ( incomingMessage.getTime() != null) {
			message.setCreatedAt(incomingMessage.getTime());
		} else {
			message.setCreatedAt(Timestamp.from(Instant.now()));
		}
		message.setContent(incomingMessage.getContent());
		message = messageRepository.save(message);
		
		return convertMessageToDto(message);
	}
	
	public void setMessageRead(IncomingMessageDTO messageDTO) {
	
	}
	
	private MessageDTO convertMessageToDto(Message message){
		return MessageDTO.builder()
				.id(message.getId())
				.senderId(message.getSender().getId())
				.recipientId(message.getRecipient().getId())
				.readAt(message.getReadAt())
				.createdAt(message.getCreatedAt())
				.content(message.getContent())
				.build();
	}
}
