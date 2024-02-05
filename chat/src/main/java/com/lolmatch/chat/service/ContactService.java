package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.ContactRepository;
import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dto.ContactDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
	
	private final ContactRepository contactRepository;
	
	private final UserService userService;
	
	private final MessageRepository messageRepository;
	
	public ContactDTO getContactListForUser(UUID id){
		User user = userService.getUserByUUID(id);
		List<Contact> contactsFromDb = contactRepository.findAllByUser(user);
		
		ContactDTO dto = new ContactDTO();
		dto.setUser(user);
		List<ContactDTO.Contact> contacts = contactsFromDb.stream().map(contact -> new ContactDTO.Contact(contact.getContactId(), contact.getContactUsername(), countMessagesBetweenUsers(user, contact.getContactId()))).collect(Collectors.toList());
		dto.setContacts(contacts);
		
		return dto;
	}
	
	private int countMessagesBetweenUsers(User user, UUID contactId){
		return messageRepository.countAllBySenderIdAndRecipientAndReadAtIsNull(contactId, user);
	}
}
