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
		User user = userService.getUserByUUID(id);
		List<Contact> contactsFromDb = contactRepository.findAllByUser(user);
		ContactListDTO dto = new ContactListDTO();
		dto.setUser(user);
		List<ContactDTO> contacts = contactsFromDb.stream().map(contact -> {
			Optional<Message> lastMessage = messageRepository.getLastMessageBetweenUsers(id, contact.getContactId());
			
			String lastMessageContent;
			UUID lastMessageSenderId;
			Timestamp lastMessageTimestamp;
			if (lastMessage.isEmpty()) {
				lastMessageContent = "";
				lastMessageSenderId = null;
				lastMessageTimestamp = null;
			} else {
				Message message = lastMessage.get();
				lastMessageContent = message.getContent();
				lastMessageSenderId = message.getSender() == user ? id : contact.getContactId();
				lastMessageTimestamp = message.getCreatedAt();
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
				lastActiveTimestamp = messageRepository.getLastMessageOfUser(id).orElse(null);
			}
			return new ContactDTO(
					contact.getContactId(),
					contact.getContactUsername(),
					"USER",
					messageRepository.countAllBySenderIdAndRecipientAndReadAtIsNull(contact.getContactId(), user),
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
			int unreadMessages = messageRepository.countAllUnreadByUserIdAndGroupId(user.getId(), group.getId());
			
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
					unreadMessages,
					lastMessage,
					lastMessageSenderId,
					activeStatus,
					lastActiveTimestamp,
					lastMessageTimestamp
			);
			contacts.add(contact);
		}
		System.out.println("TEST");
		contacts.sort((a, b) -> {
			if ( a.lastMessageTimestamp() == null || b.lastMessageTimestamp() == null){
				return 0;
			}
			return a.lastMessageTimestamp().compareTo(b.lastMessageTimestamp());
		});
		dto.setContacts(contacts);
		
		return dto;
	}
}
