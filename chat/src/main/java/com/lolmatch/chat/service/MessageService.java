package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.GroupRepository;
import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dao.ReadStatusRepository;
import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.dto.*;
import com.lolmatch.chat.entity.Group;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.ReadStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
	public FetchMessagesDTO getMessageListBetweenUsers(UUID firstId, UUID secondId, Optional<Integer> size, Optional<Integer> page) {
		int pageNumber = page.orElse(0);
		int pageSize = size.orElse(20);
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Message> messagePage;
		if ( userRepository.existsById(secondId)) {
			messagePage = messageRepository.getMessagesBetweenUsersPaged(firstId, secondId, pageable);
		} else {
			messagePage = messageRepository.findAllByGroupRecipientIdOrderByCreatedAtDesc(secondId, pageable);
		}
		return FetchMessagesDTO.builder()
				.amount(messagePage.getSize())
				.page(messagePage.getNumber())
				.messages(messagePage.getContent())
				.totalMessages(messagePage.getTotalElements())
				.build();
	}
	
	@Transactional
	public void saveMessage(IncomingMessageDTO incomingMessage) {
		Message message = new Message();
		message.setSender(userRepository.getReferenceById(incomingMessage.getSenderId()));
		message.setCreatedAt(setTime(incomingMessage));
		message.setContent(incomingMessage.getContent());
		if (incomingMessage.getParentId() != null) {
			message.setParentMessage(messageRepository.getReferenceById(incomingMessage.getParentId()));
		}
		OutgoingMessageDTO dto = convertMessageToDto(messageRepository.save(message));
		
		messagingTemplate.convertAndSend("/topic/chat/" + String.valueOf(dto.getRecipientId()), dto);
		messagingTemplate.convertAndSend("/topic/chat/" + String.valueOf(dto.getSenderId()), dto);
	}
	
	@Transactional
	public void setMessageRead(IncomingMessageDTO incomingMessage) {
		final Timestamp time = setTime(incomingMessage);
		messageRepository.setReadTimestamp(time, incomingMessage.getSenderId(), incomingMessage.getRecipientId());
		MessageReadDTO dto = new MessageReadDTO(incomingMessage.getSenderId(), incomingMessage.getRecipientId(), time);
		
		messagingTemplate.convertAndSend("/topic/chat/" + String.valueOf(dto.getSenderId()), dto);
	}
	
	@Transactional
	public void saveMessageGroup(IncomingMessageDTO incomingMessage) {
		Group group = groupRepository.findById(incomingMessage.getRecipientId()).orElseThrow(() -> new EntityNotFoundException("No group found"));
		Message message = new Message();
		message.setGroupRecipient(group);
		message.setCreatedAt(setTime(incomingMessage));
		message.setContent(incomingMessage.getContent());
		if (incomingMessage.getParentId() != null) {
			message.setParentMessage(messageRepository.getReferenceById(incomingMessage.getParentId()));
		}
		OutgoingMessageDTO dto = convertMessageToDto(messageRepository.save(message));
		
		group.getUsers().forEach(user -> {
			SimpUser simpUser = userRegistry.getUser(user.getId().toString());
			if (simpUser != null && simpUser.hasSessions()) {
				messagingTemplate.convertAndSend("/topic/chat/" + String.valueOf(user.getId()), dto);
			}
		});
	}
	
	@Transactional
	public void setMessageReadGroup(IncomingMessageDTO incomingMessage) {
		Timestamp time = setTime(incomingMessage);
		// zapytanie poniżej powinno znaleźć wszystkie wiadomości grupy które nie zostały odczytane przez danego użytkownika
		List<Message> unreadMessages = messageRepository.findAllGroupMessagesUnreadByUserIdAndGroupId(incomingMessage.getRecipientId(), incomingMessage.getSenderId());
		for ( Message unreadMessage: unreadMessages){
			ReadStatus readStatus = new ReadStatus();
			readStatus.setUserId(incomingMessage.getRecipientId());
			readStatus.setReadAt(time);
			readStatus.setMessage(unreadMessage);
			
			readStatusRepository.save(readStatus);
		}
		// send info to all other users that all messages have been read
		Group group = groupRepository.findById(incomingMessage.getRecipientId()).orElseThrow(() -> new EntityNotFoundException("No group found"));
		group.getUsers().forEach(user -> {
			SimpUser simpUser = userRegistry.getUser(user.getId().toString());
			if (simpUser != null && simpUser.hasSessions() && !user.getId().equals(incomingMessage.getRecipientId())) {
				MessageReadDTO dto = new MessageReadDTO(incomingMessage.getSenderId(), incomingMessage.getRecipientId(), time);
				messagingTemplate.convertAndSend("/topic/chat/" + String.valueOf(user.getId()), dto);
			}
		});
	}
	
	private OutgoingMessageDTO convertMessageToDto(Message message) {
		return OutgoingMessageDTO.builder()
				.id(message.getId())
				.senderId(message.getSender().getId())
				.recipientId(message.getRecipient().getId())
				.readAt(message.getReadAt())
				.createdAt(message.getCreatedAt())
				.content(message.getContent())
				.readAtList(convertReadStatusDtoList(message.getReadStatuses()))
				.build();
	}
	
	private ReadStatusDTO convertReadStatusToDto(ReadStatus status){
		return new ReadStatusDTO(status.getId(), status.getUserId(), status.getReadAt(), status.getMessage().getId());
	}
	
	private List<ReadStatusDTO> convertReadStatusDtoList(List<ReadStatus> list){
		return list.stream().map(this::convertReadStatusToDto).toList();
	}
	
	private Timestamp setTime(IncomingMessageDTO dto) {
		if (dto.getTime() == null) {
			return Timestamp.from(Instant.now());
		} else {
			return Timestamp.from(dto.getTime());
		}
	}
}
