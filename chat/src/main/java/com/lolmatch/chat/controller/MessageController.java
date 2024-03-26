package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.FetchMessagesDTO;
import com.lolmatch.chat.dto.IncomingMessageDTO;
import com.lolmatch.chat.service.MessageService;
import com.lolmatch.chat.util.ActionTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
	
	private final MessageService messageService;
	
	@GetMapping()
	@PreAuthorize("#firstUserId.toString() == #principal.name.toString() || #secondUserId.toString() == #principal.name.toString()")
	public FetchMessagesDTO getMessages(
			@RequestParam("firstUserId") UUID firstUserId,
			@RequestParam("secondUserId") UUID secondUserId, // if looking for group messages this is group id
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("page") Optional<Integer> page,
			Principal principal
	){
		log.info("Get messages request, details: firstId- " + firstUserId + ";secondId- " + secondUserId + ";size " + size + ";page " + page);
		return messageService.getMessageListBetweenUsers(firstUserId, secondUserId, size, page);
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
