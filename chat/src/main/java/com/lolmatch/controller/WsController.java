package com.lolmatch.controller;

import com.lolmatch.dto.IncomingMessageDTO;
import com.lolmatch.dto.TestDTO;
import com.lolmatch.service.MessageService;
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
	
	private final SimpMessagingTemplate messagingTemplate;
	
	private final SimpUserRegistry userRegistry;
	
	@MessageMapping("/chat")

	public void processMessage(@Payload TestDTO messageDTO, @Header("simpSessionId") String sessionId) {
		//if( messageDTO.getType() == ActionTypeEnum.SEND){
			// TODO - save message in db then try to send it to recipient
			//messageService.saveMessage(messageDTO);
		//} else if ( messageDTO.getType() == ActionTypeEnum.MARK_READ){
			// TODO - set all messages with given users before given timestamp as read
			//messageService.setMessageRead(messageDTO);
		//}
		// TODO - get recipientId from messageDTO and send message to this session
		messagingTemplate.convertAndSendToUser( "user","/topic/chat" , messageDTO);
	}

}