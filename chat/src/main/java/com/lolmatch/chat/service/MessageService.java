package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dto.FetchMessagesDTO;
import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.dto.MessageReadDTO;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	
	private final UserService userService;
	
	private final EntityManager entityManager;
	
	public FetchMessagesDTO getListOfMessages(UUID firstId, UUID secondId, Optional<Integer> size, Optional<Integer> page) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
		Root<Message> messageRoot = criteriaQuery.from(Message.class);
		
		Predicate senderIdPredicate = criteriaBuilder.equal(messageRoot.get("sender").get("id"), firstId);
		Predicate recipientIdPredicate = criteriaBuilder.equal(messageRoot.get("recipient").get("id"), secondId);
		Predicate firstAndPredicate = criteriaBuilder.and(senderIdPredicate, recipientIdPredicate);
		
		Predicate secondSenderIdPredicate = criteriaBuilder.equal(messageRoot.get("sender").get("id"), secondId);
		Predicate secondRecipientIdPredicate = criteriaBuilder.equal(messageRoot.get("recipient").get("id"), firstId);
		Predicate secondAndPredicate = criteriaBuilder.and(secondSenderIdPredicate, secondRecipientIdPredicate);
		
		Predicate finalPredicate = criteriaBuilder.or(firstAndPredicate, secondAndPredicate);
		// final predicate equals to (firstId AND secondId) OR (secondId AND firstId)
		
		criteriaQuery.where(finalPredicate);
		criteriaQuery.orderBy(criteriaBuilder.desc(messageRoot.get("createdAt")));
		Query query = entityManager.createQuery(criteriaQuery);
		int pageNumber = page.orElse(0);
		int sizeOfPage = size.orElse(20);
		
		query.setFirstResult((pageNumber) * sizeOfPage);
		query.setMaxResults(sizeOfPage);
		
		List<Message> messages = query.getResultList();
		
		long totalMessages = Long.sum(
				messageRepository.countAllBySenderIdAndRecipientId(firstId, secondId),
				messageRepository.countAllBySenderIdAndRecipientId(secondId, firstId)
		);
		
		return FetchMessagesDTO.builder()
				.amount(sizeOfPage)
				.page(pageNumber)
				.messages(messages)
				.totalMessages(totalMessages)
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
	
	public int countMessagesBetweenUsers(User user, UUID contactId){
		return messageRepository.countAllBySenderIdAndRecipientAndReadAtIsNull(contactId, user);
	}
	
	public MessageReadDTO setMessageRead(IncomingMessageDTO messageDTO) {
		List<Message> messageList = messageRepository.findAllBySenderIdAndRecipientIdAndReadAtIsNull(
				messageDTO.getSenderId(),
				messageDTO.getRecipientId()
		);
		
		messageList.stream().forEach(message -> {
			if ( messageDTO.getTime() != null){
				message.setReadAt(Timestamp.from(messageDTO.getTime()));
			} else {
				message.setReadAt(Timestamp.from(Instant.now()));
			}
			messageRepository.save(message);
		});
		Timestamp time;
		if ( messageDTO.getTime() == null){
			time = Timestamp.from(Instant.now());
		} else {
			time = Timestamp.from(messageDTO.getTime());
		}
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
