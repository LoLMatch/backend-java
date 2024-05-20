package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.GroupRepository;
import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dao.ReadStatusRepository;
import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.dto.MessageListDTO;
import com.lolmatch.chat.dto.MessageReadDTO;
import com.lolmatch.chat.entity.Group;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.ReadStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
	private final MessageRepository messageRepository;
	
	private final UserRepository userRepository;
	
	private final GroupRepository groupRepository;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	private final SimpUserRegistry userRegistry;
	
	private final ReadStatusRepository readStatusRepository;
	
	@Transactional
	public MessageListDTO getMessageListBetweenUsers(UUID firstId, UUID secondId, Pageable pageable) {
		if (userRepository.existsById(secondId)) {
			// fetch for users
			Page<MessageDTO> messagePage;
			messagePage = messageRepository.getMessagesBetweenUsersPaged(firstId, secondId, pageable);
			return new MessageListDTO(messagePage.getContent(), messagePage.getNumber(), messagePage.getSize(), messagePage.getTotalElements());
		} else {
			// fetch for group
			Page<Message> messagePage;
			messagePage = messageRepository.findAllByGroupRecipientIdOrderByCreatedAtDesc(secondId, pageable);
			return new MessageListDTO(messageListToDto(messagePage.getContent(), "MESSAGE_GROUP"), messagePage.getNumber(), messagePage.getSize(), messagePage.getTotalElements());
		}
	}
	
	@Transactional
	public void saveMessage(IncomingMessageDTO incomingMessage) {
		Message message = new Message();
		message.setSender(userRepository.getReferenceById(incomingMessage.senderId()));
		message.setCreatedAt(setTime(incomingMessage));
		message.setContent(incomingMessage.content());
		message.setRecipient(userRepository.getReferenceById(incomingMessage.recipientId()));
		if (incomingMessage.parentId() != null) {
			message.setParentMessage(messageRepository.getReferenceById(incomingMessage.parentId()));
		}
		MessageDTO dto = messageRepository.save(message).toDto("MESSAGE");
		
		messagingTemplate.convertAndSend("/topic/chat/" + dto.recipientId(), dto);
		messagingTemplate.convertAndSend("/topic/chat/" + dto.senderId(), dto);
	}
	
	@Transactional
	public void setMessageRead(IncomingMessageDTO incomingMessage) {
		final Timestamp time = setTime(incomingMessage);
		messageRepository.setReadTimestamp(time, incomingMessage.senderId(), incomingMessage.recipientId());
		MessageReadDTO dto = new MessageReadDTO(incomingMessage.senderId(), incomingMessage.recipientId(), time);
		
		messagingTemplate.convertAndSend("/topic/chat/" + dto.senderId(), dto);
	}
	
	@Transactional
	public void saveMessageGroup(IncomingMessageDTO incomingMessage) {
		Group group = groupRepository.findById(incomingMessage.recipientId()).orElseThrow(() -> new EntityNotFoundException("No group found"));
		Message message = new Message();
		message.setSender(userRepository.getReferenceById(incomingMessage.senderId()));
		message.setGroupRecipient(group);
		message.setCreatedAt(setTime(incomingMessage));
		message.setContent(incomingMessage.content());
		if (incomingMessage.parentId() != null) {
			message.setParentMessage(messageRepository.getReferenceById(incomingMessage.parentId()));
		}
		MessageDTO dto = messageRepository.save(message).toDto("MESSAGE_GROUP");
		
		group.getUsers().forEach(user -> {
			SimpUser simpUser = userRegistry.getUser(user.getId().toString());
			if (simpUser != null && simpUser.hasSessions()) {
				messagingTemplate.convertAndSend("/topic/chat/" + user.getId(), dto);
			}
		});
	}
	
	@Transactional
	public void setMessageReadGroup(IncomingMessageDTO incomingMessage) {
		Timestamp time = setTime(incomingMessage);
		List<Message> unreadMessages = messageRepository.findAllGroupMessagesUnreadByUserIdAndGroupId(incomingMessage.recipientId(), incomingMessage.senderId());
		for (Message unreadMessage : unreadMessages) {
			ReadStatus readStatus = new ReadStatus();
			readStatus.setUserId(incomingMessage.recipientId());
			readStatus.setReadAt(time);
			readStatus.setMessage(unreadMessage);
			
			readStatusRepository.save(readStatus);
		}
		// send info to all other users that all messages have been read
		Group group = groupRepository.findById(incomingMessage.senderId()).orElseThrow(() -> new EntityNotFoundException("No group found"));
		group.getUsers().forEach(user -> {
			SimpUser simpUser = userRegistry.getUser(user.getId().toString());
			if (simpUser != null && simpUser.hasSessions() && !user.getId().equals(incomingMessage.recipientId())) {
				MessageReadDTO dto = new MessageReadDTO(incomingMessage.senderId(), incomingMessage.recipientId(), time);
				messagingTemplate.convertAndSend("/topic/chat/" + String.valueOf(user.getId()), dto);
			}
		});
	}
	
	private List<MessageDTO> messageListToDto(List<Message> messages, String action) {
		if (messages.isEmpty()) {
			return Collections.emptyList();
		}
		return messages.stream().map( m -> m.toDto(action)).toList();
	}
	
	private Timestamp setTime(IncomingMessageDTO dto) {
		if (dto.time() == null) {
			return Timestamp.from(Instant.now());
		} else {
			return Timestamp.from(dto.time());
		}
	}
}
