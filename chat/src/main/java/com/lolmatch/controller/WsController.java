package com.lolmatch.controller;

import com.lolmatch.dto.IncomingMessageDTO;
import com.lolmatch.dto.MessageDTO;
import com.lolmatch.dto.TestDTO;
import com.lolmatch.service.MessageService;
import com.lolmatch.service.UserService;
import com.lolmatch.util.ActionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WsController {
	
	private final MessageService messageService;
	
	private final UserService userService;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chat")
	public void processMessage(@Payload IncomingMessageDTO message) {
		if( message.getType() == ActionTypeEnum.SEND){
			// TODO - save message in db then try to send it to recipient
			MessageDTO outgoingMessage = messageService.saveMessage(message);
			String recipientUsername = userService.getUsernameByUUID(message.getRecipientId());
			messagingTemplate.convertAndSendToUser(recipientUsername, "/topic/chat", outgoingMessage);
		} else if ( message.getType() == ActionTypeEnum.MARK_READ){
			// TODO - set all messages with given user and before given timestamp as read
			messageService.setMessageRead(message);
		}
	}

}