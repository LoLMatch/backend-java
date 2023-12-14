package com.lolmatch.controller;

import com.lolmatch.dto.FetchMessagesDTO;
import com.lolmatch.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {
	
	private final MessageService messageService;
	
	@GetMapping("/messages")
	public FetchMessagesDTO getMessages(
			@RequestParam("senderId") UUID senderId,
			@RequestParam("recipientId") UUID recipientId,
			@RequestParam("size") Optional<Integer> size
	){
		// TODO - this method will return list of messages according to search criteria from request params
		log.info("Get messages request, details: senderId-" + senderId + ";recipientId-" + recipientId + ";size" + size);
		return messageService.getListOfMessages(senderId, recipientId, size);
	}
}
