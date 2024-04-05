package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.ContactRepository;
import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dto.ContactChangeDTO;
import com.lolmatch.chat.dto.ContactDTO;
import com.lolmatch.chat.dto.ContactListDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.entity.Group;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
	private final ContactRepository contactRepository;
	
	private final UserService userService;
	
	private final MessageRepository messageRepository;
	
	private final SimpUserRegistry simpUserRegistry;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	public List<Contact> getContactsForUser(UUID id) {
		return contactRepository.findAllByUserId(id);
	}
	// TODO - to jest do zapisywania kontaktów jak będą przychodzić z kolejki
	public void saveContact(UUID firstUserId, UUID secondUserId) {
		User first = userService.getUserByUUID(firstUserId);
		User second = userService.getUserByUUID(secondUserId);
		
		Contact firstContact = new Contact();
		firstContact.setUser(first);
		firstContact.setContactId(second.getId());
		firstContact.setContactUsername(second.getUsername());
		
		Contact secondContact = new Contact();
		secondContact.setUser(second);
		secondContact.setContactId(first.getId());
		secondContact.setContactUsername(first.getUsername());
		
		contactRepository.save(firstContact);
		contactRepository.save(secondContact);
		
		SimpUser firstSimpUser = simpUserRegistry.getUser(firstUserId.toString());
		SimpUser secondSimpUser = simpUserRegistry.getUser(secondUserId.toString());
		if (firstSimpUser != null && firstSimpUser.hasSessions()) {
			ContactChangeDTO dto = new ContactChangeDTO(ContactChangeDTO.ActionType.CONTACT_ADDED, ContactChangeDTO.ContactType.USER, second.getId(), second.getUsername());
			messagingTemplate.convertAndSend("/topic/chat/" + firstUserId, dto);
		}
		if (secondSimpUser != null && secondSimpUser.hasSessions()) {
			ContactChangeDTO dto = new ContactChangeDTO(ContactChangeDTO.ActionType.CONTACT_ADDED, ContactChangeDTO.ContactType.USER, first.getId(), first.getUsername());
			messagingTemplate.convertAndSend("/topic/chat/" + secondUserId, dto);
		}
	}
	
	@Transactional
	public ContactListDTO getContactListForUser(UUID id) {
		// TODO - tutaj powinna być paginacja
		User user = userService.getUserByUUID(id);
		List<Contact> contactsFromDb = contactRepository.findAllByUser(user);
		Map<UUID, Long> unreadMessages = messageRepository.countUnreadForContactsOfUser(id)
				.stream()
				.map(result -> {
					UUID userId = (UUID) result[1];
					Long unreadMessagesCount = (Long) result[0];
					return new UnreadMessages(unreadMessagesCount, userId);
				})
				.collect(Collectors.toMap(UnreadMessages::userId, UnreadMessages::unreadMessagesCount));
		Map<UUID, Message> lastMessages = messageRepository.getLastMessagesBetweenUserAndContact(id)
				.stream()
				.map(message -> {
					if (message.getRecipient().getId() != id) {
						return new MessageContactRecord(message.getRecipient().getId(), message);
					} else {
						return new MessageContactRecord(message.getSender().getId(), message);
					}
				})
				.collect(Collectors.toMap(MessageContactRecord::userId, MessageContactRecord::message));
		List<ContactDTO> contacts = contactsFromDb.stream().map(contact -> {
			Message lastMessage = lastMessages.getOrDefault(contact.getContactId(), null);
			String lastMessageContent;
			UUID lastMessageSenderId;
			Timestamp lastMessageTimestamp;
			if (lastMessage == null) {
				lastMessageContent = "";
				lastMessageSenderId = null;
				lastMessageTimestamp = null;
			} else {
				lastMessageContent = lastMessage.getContent();
				lastMessageSenderId = lastMessage.getSender().getId() == id ? id : contact.getContactId();
				lastMessageTimestamp = lastMessage.getCreatedAt();
			}
			SimpUser simpUser = simpUserRegistry.getUser(String.valueOf(contact.getContactId()));
			boolean isActive;
			Timestamp lastActiveTimestamp;
			if (simpUser != null) {
				isActive = simpUser.hasSessions();
			} else {
				isActive = false;
			}
			if (isActive) {
				lastActiveTimestamp = null;
			} else {
				if (lastMessage == null) {
					lastActiveTimestamp = null;
				} else {
					lastActiveTimestamp = lastMessage.getCreatedAt();
				}
			}
			return new ContactDTO(
					contact.getContactId(),
					contact.getContactUsername(),
					"USER",
					unreadMessages.get(contact.getContactId()),
					lastMessageContent,
					lastMessageSenderId,
					isActive,
					lastActiveTimestamp,
					lastMessageTimestamp
			);
		}).collect(Collectors.toList());
		if (user.getGroup() != null) {
			Group group = user.getGroup();
			Optional<Message> lastGroupMessage = messageRepository.getLastMessageInGroup(group.getId());
			Timestamp lastMessageTimestamp;
			String lastMessage;
			UUID lastMessageSenderId;
			if (lastGroupMessage.isPresent()) {
				lastMessage = lastGroupMessage.get().getContent();
				lastMessageTimestamp = lastGroupMessage.get().getCreatedAt();
				lastMessageSenderId = lastGroupMessage.get().getSender().getId();
			} else {
				lastMessage = "";
				lastMessageTimestamp = null;
				lastMessageSenderId = null;
			}
			Long unreadMessagesInGroup = messageRepository.countAllUnreadByUserIdAndGroupId(user.getId(), group.getId());
			
			boolean activeStatus = group.getUsers().stream().anyMatch(groupUser -> {
				SimpUser simpUser = simpUserRegistry.getUser(groupUser.getId().toString());
				return simpUser != null && simpUser.hasSessions();
			});
			Timestamp lastActiveTimestamp;
			if (activeStatus) {
				lastActiveTimestamp = null;
			} else {
				lastActiveTimestamp = lastMessageTimestamp;
			}
			ContactDTO contact = new ContactDTO(
					group.getId(),
					group.getName(),
					"GROUP",
					unreadMessagesInGroup,
					lastMessage,
					lastMessageSenderId,
					activeStatus,
					lastActiveTimestamp,
					lastMessageTimestamp
			);
			contacts.add(contact);
		}
		contacts.sort((a, b) -> {
			if (a.lastMessageTimestamp() == null || b.lastMessageTimestamp() == null) {
				return 0;
			}
			return b.lastMessageTimestamp().compareTo(a.lastMessageTimestamp());
		});
		
		return new ContactListDTO(user, contacts);
	}
	
	private record MessageContactRecord(UUID userId, Message message) {
	}
	private record UnreadMessages(long unreadMessagesCount, UUID userId) {
	}
}
