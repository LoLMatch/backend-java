package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.ContactDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {
	
	private final ContactService contactService;
	
	@GetMapping()
	public ContactDTO getContactsOfUser(@RequestParam("id")UUID id){
		return contactService.getContactListForUser(id);
	}
}
