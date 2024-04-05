package com.lolmatch.chat.service;

import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.util.UserStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	private final ContactService contactService;
	
	@EventListener
	public void handleUserStatusChange(UserStatusChangeEvent event) {
		List<Contact> contacts = contactService.getContactsForUser(UUID.fromString(event.getStatusChangeDTO().id()));
		log.info("Status of user: " + event.getStatusChangeDTO().id() + ", changed to: " + event.getStatusChangeDTO().status());
		for (Contact contact : contacts) {
			messagingTemplate.convertAndSend("/topic/chat/" + contact.getContactId(), event.getStatusChangeDTO());
		}
	}
}
