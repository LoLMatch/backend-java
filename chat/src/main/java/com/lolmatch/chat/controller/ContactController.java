package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.ContactDTO;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {
	
	private final ContactService contactService;
	
	@GetMapping()
	public ContactDTO getContactsOfUser(@RequestParam("id")UUID id, Principal principal){
		if( !principal.getName().equals(id.toString())){
			log.warn("Cannot access contacts of another user: " + id + ", from user: " + principal.getName());
			throw new AccessDeniedException("Cannot access contacts of another user: " + id + ", from user: " + principal.getName());
		}
		return contactService.getContactListForUser(id);
	}
}
