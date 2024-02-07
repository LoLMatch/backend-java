package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.ContactRepository;
import com.lolmatch.chat.dto.ContactDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
	
	private final ContactRepository contactRepository;
	
	private final UserService userService;
	
	private final MessageService messageService;
	
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
			List<Message> messages = messageService.getListOfMessages(
					id,
					contact.getContactId(),
					Optional.of(1),
					Optional.of(0)).getMessages();
			String lastMessageContent;
			UUID lastMessageSenderId;
			if ( messages.isEmpty()){
				lastMessageContent = "";
				lastMessageSenderId = null;
			} else {
				Message message = messages.get(0);
				lastMessageContent = message.getContent();
				lastMessageSenderId = message.getSender() == user ?  id : contact.getContactId();
			}
			SimpUser simpUser = simpUserRegistry.getUser(String.valueOf(contact.getContactId()));
			System.out.println(simpUser);
			Boolean isActive;
			if ( simpUser != null) {
				isActive = simpUser.hasSessions();
			} else {
				isActive = false;
			}
			return new ContactDTO.Contact(
					contact.getContactId(),
					contact.getContactUsername(),
					messageService.countMessagesBetweenUsers(user, contact.getContactId()),
					lastMessageContent,
					lastMessageSenderId,
					isActive);
		}).collect(Collectors.toList());
		dto.setContacts(contacts);
		
		return dto;
	}
}
