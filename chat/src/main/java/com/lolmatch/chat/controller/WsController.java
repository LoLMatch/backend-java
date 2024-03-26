package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Slf4j
@Controller
@RequiredArgsConstructor
public class WsController {
	
	private final MessageService messageService;
	
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
				messageService.saveMessage(message);
			}
			case MARK_READ -> {
				messageService.setMessageRead(message);
			}
			case SEND_GROUP -> {
				messageService.saveMessageGroup(message);
			}
			case MARK_READ_GROUP -> {
				messageService.setMessageReadGroup(message);
			}
			default -> {
				log.error("Wrong action type on incoming message: " + message);
				throw new IllegalStateException("Wrong action type on incoming message - " + message.getType());
			}
		}
	}

}