package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.FetchMessagesDTO;
import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.service.MessageService;
import com.lolmatch.chat.util.ActionTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {
	
	private final MessageService messageService;
	
	@GetMapping("/messages")
	public FetchMessagesDTO getMessages(
			@RequestParam("firstUserId") UUID firstUserId,
			@RequestParam("secondUserId") UUID secondUserId,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("page") Optional<Integer> page,
			Principal principal
	){
		if ( !principal.getName().equals(firstUserId.toString()) && !principal.getName().equals(secondUserId.toString())){
			log.warn("Cannot access messages of another users, principal: " + principal.getName());
			throw new AccessDeniedException("Cannot access messages of another users, principal: " + principal.getName());
		}
		log.info("Get messages request, details: firstId- " + firstUserId + ";secondId- " + secondUserId + ";size " + size + ";page " + page);
		return messageService.getListOfMessages(firstUserId, secondUserId, size, page);
	}
	
	@PostMapping("/read-messages")
	public String readMessagesTest(@RequestBody IncomingMessageDTO messageDTO){
		// use for test of setting message as read, may be deleted later
		if ( messageDTO.getType().equals(ActionTypeEnum.MARK_READ)) {
			messageService.setMessageRead(messageDTO);
			return "OK";
		}
		return "NOTHING";
	}
}
