package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.dto.UserStatusChangeDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.service.ContactService;
import com.lolmatch.chat.service.MessageService;
import com.lolmatch.chat.service.UserService;
import com.lolmatch.chat.util.ActionTypeEnum;
import com.lolmatch.chat.util.UserStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
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
	public void processMessage(@Payload IncomingMessageDTO message, Principal principal) {
		if ( principal == null) {
			log.warn("Unauthenticated user tried to send message: " + message.toString());
			throw new AccessDeniedException("Unauthenticated user tried to send message: " + message.toString());
		}
		if ( !principal.getName().equals(message.getSenderId().toString())){
			log.warn("Trying to send message from: " + principal.getName() + ", send as: " + message.getSenderId().toString());
			throw new AccessDeniedException("Wrong user tried to send message: " + message.toString());
		}
		log.info("Incoming message on channel /app/chat, details: "  + message.toString());
		if( message.getType() == ActionTypeEnum.SEND){
			MessageDTO outgoingMessage = messageService.saveMessage(message);
			messagingTemplate.convertAndSend("/topic/chat/"+String.valueOf(outgoingMessage.getRecipientId()), outgoingMessage);
		} else if ( message.getType() == ActionTypeEnum.MARK_READ){
			messageService.setMessageRead(message);
		} else {
			log.error("Wrong action type on incoming message: " + message.toString());
			throw new IllegalStateException("Wrong action type on incoming message - " + message.getType());
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