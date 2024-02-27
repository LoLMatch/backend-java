package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dto.FetchMessagesDTO;
import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.dto.MessageReadDTO;
import com.lolmatch.chat.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	public FetchMessagesDTO getMessageListBetweenUsers(UUID firstId, UUID secondId, Optional<Integer> size, Optional<Integer> page) {
		int pageNumber = page.orElse(0);
		int pageSize = size.orElse(20);
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		Page<Message> messagePage = messageRepository.getMessagesBetweenUsersPaged(firstId,secondId,pageable);
		
		return FetchMessagesDTO.builder()
				.amount(messagePage.getSize())
				.page(messagePage.getNumber())
				.messages(messagePage.getContent())
				.totalMessages(messagePage.getTotalElements())
				.build();
	}
	
	public MessageDTO saveMessage(IncomingMessageDTO incomingMessage) {
		Message message = new Message();
		message.setSender(userService.getUserByUUID(incomingMessage.getSenderId()));
		message.setRecipient(userService.getUserByUUID(incomingMessage.getRecipientId()));
		if ( incomingMessage.getTime() != null) {
			message.setCreatedAt(Timestamp.from(incomingMessage.getTime()));
		} else {
			message.setCreatedAt(Timestamp.from(Instant.now()));
		}
		message.setContent(incomingMessage.getContent());
		message = messageRepository.save(message);
		
		return convertMessageToDto(message);
	}
	
	public MessageReadDTO setMessageRead(IncomingMessageDTO messageDTO) {
		final Timestamp time;
		if ( messageDTO.getTime() == null){
			time = Timestamp.from(Instant.now());
		} else {
			time = Timestamp.from(messageDTO.getTime());
		}
		messageRepository.setReadTimestamp(time, messageDTO.getSenderId(), messageDTO.getRecipientId());

		return new MessageReadDTO(messageDTO.getSenderId(), messageDTO.getRecipientId(), time);
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
