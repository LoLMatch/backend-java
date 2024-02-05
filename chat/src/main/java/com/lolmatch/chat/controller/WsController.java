package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.service.MessageService;
import com.lolmatch.chat.service.UserService;
import com.lolmatch.chat.util.ActionTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
@RequiredArgsConstructor
public class WsController {
	
	private final MessageService messageService;
	
	private final UserService userService;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chat")
	public void processMessage(@Payload IncomingMessageDTO message) {
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

}