package com.lolmatch.controller;

import com.lolmatch.dto.IncomingMessageDTO;
import com.lolmatch.service.MessageService;
import com.lolmatch.util.ActionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WsController {
	
	private final MessageService messageService;
	
	@MessageMapping("/chat")
	@SendToUser("/topic/public")
	public void processMessage(IncomingMessageDTO messageDTO) {
		// TODO - here message will be processed, saved and send further
		if( messageDTO.getType() == ActionTypeEnum.SEND){
			// TODO - save message in db then try to send it to recipient
			messageService.saveMessage(messageDTO);
		} else if ( messageDTO.getType() == ActionTypeEnum.MARK_READ){
			// TODO - set all messages with given users before given timestamp as read
			messageService.setMessageRead(messageDTO);
		}
	}

}