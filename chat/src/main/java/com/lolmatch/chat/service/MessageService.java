package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dto.FetchMessagesDTO;
import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	
	private final UserService userService;
	
	public FetchMessagesDTO getListOfMessages(UUID senderId, UUID recipientId, Optional<Integer> size) {
		// TODO - to be implemented
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
		User sender = userService.getUserByUUID(messageDTO.getSenderId());
		User recipient = userService.getUserByUUID(messageDTO.getRecipientId());

		List<Message> messageList = messageRepository.findAllBySenderAndRecipientAndReadAtIsNull(sender, recipient);
		messageList.stream().forEach(message -> {
			if ( messageDTO.getTime() != null){
				message.setReadAt(messageDTO.getTime());
			} else {
				message.setReadAt(Timestamp.from(Instant.now()));
			}
			messageRepository.save(message);
		});
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
