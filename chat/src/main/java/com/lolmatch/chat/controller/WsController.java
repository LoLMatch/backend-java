package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.OutgoingMessageDTO;
import com.lolmatch.chat.dto.MessageReadDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.service.ContactService;
import com.lolmatch.chat.service.MessageService;
import com.lolmatch.chat.util.UserStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


@Slf4j
@Controller
@RequiredArgsConstructor
public class WsController {
	
	private final MessageService messageService;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	private final ContactService contactService;
	
	@MessageMapping("/chat")
	@PreAuthorize("""
	(#message.senderId.toString() == #principal.name.toString() && #message.type.toString() == 'SEND')
	||
	(#message.recipientId.toString() == #principal.name.toString() && #message.type.toString() == 'MARK_READ')
	""")
	public void processMessage(@Payload IncomingMessageDTO message, Principal principal) {
		log.info("Incoming message on channel /app/chat, details: "  + message.toString());
		switch ( message.getType()){
			case SEND -> {
				OutgoingMessageDTO outgoingMessage = messageService.saveMessage(message);
				messagingTemplate.convertAndSend("/topic/chat/"+String.valueOf(outgoingMessage.getRecipientId()), outgoingMessage);
			}
			case MARK_READ -> {
				MessageReadDTO outgoingMessage = messageService.setMessageRead(message);
				messagingTemplate.convertAndSend("/topic/chat/"+String.valueOf(outgoingMessage.getRecipientId()), outgoingMessage);
			}
			default -> {
				log.error("Wrong action type on incoming message: " + message);
				throw new IllegalStateException("Wrong action type on incoming message - " + message.getType());
			}
		}
	}
	
	@EventListener
	public void handleUserStatusChange(UserStatusChangeEvent event) {
		List<Contact> contacts = contactService.getContactsForUser(UUID.fromString(event.getStatusChangeDTO().getId()));
		log.info("Status of user: " + event.getStatusChangeDTO().getId() + ", changed to: " + event.getStatusChangeDTO().getStatus());
		for ( Contact contact: contacts) {
			messagingTemplate.convertAndSend("/topic/chat/" + contact.getContactId(), event.getStatusChangeDTO());
		}
	}

}