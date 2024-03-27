package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.ContactListDTO;
import com.lolmatch.chat.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {
	
	private final ContactService contactService;
	
	@GetMapping()
	@PreAuthorize("#id.toString() == #principal.name.toString()")
	public ContactListDTO getContactsOfUser(@RequestParam("id")UUID id, Principal principal){
		log.info("Get contacts request, details: id - " + id + ", principal - " + principal.toString());
		return contactService.getContactListForUser(id);
	}
}
