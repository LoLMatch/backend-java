package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.ContactRepository;
import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dto.ContactDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

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
	
	public List<Contact> getContactsForUser(UUID id){
		return contactRepository.findAllByUserId(id);
	}
	
	public ContactDTO getContactListForUser(UUID id){
		User user = userService.getUserByUUID(id);
		List<Contact> contactsFromDb = contactRepository.findAllByUser(user);
		ContactDTO dto = new ContactDTO();
		dto.setUser(user);
		List<ContactDTO.Contact> contacts = contactsFromDb.stream().map(contact -> {
			Optional<Message> lastMessage = messageRepository.getLastMessageBetweenUsers(id, contact.getContactId());
			
			String lastMessageContent;
			UUID lastMessageSenderId;
			Timestamp lastMessageTimestamp;
			if ( lastMessage.isEmpty()){
				lastMessageContent = "";
				lastMessageSenderId = null;
				lastMessageTimestamp = null;
			} else {
				Message message = lastMessage.get();
				lastMessageContent = message.getContent();
				lastMessageSenderId = message.getSender() == user ?  id : contact.getContactId();
				lastMessageTimestamp = message.getCreatedAt();
			}
			SimpUser simpUser = simpUserRegistry.getUser(String.valueOf(contact.getContactId()));
			boolean isActive;
			Timestamp lastActiveTimestamp;
			if ( simpUser != null) {
				isActive = simpUser.hasSessions();
			} else {
				isActive = false;
			}
			if ( isActive){
				lastActiveTimestamp = null;
			} else {
				lastActiveTimestamp = messageRepository.getLastMessageOfUser(id).orElse(null);
			}
			return new ContactDTO.Contact(
					contact.getContactId(),
					contact.getContactUsername(),
					messageRepository.countAllBySenderIdAndRecipientAndReadAtIsNull(contact.getContactId(), user),
					lastMessageContent,
					lastMessageSenderId,
					isActive,
					lastActiveTimestamp,
					lastMessageTimestamp);
		}).collect(Collectors.toList());
		dto.setContacts(contacts);
		
		return dto;
	}
}
