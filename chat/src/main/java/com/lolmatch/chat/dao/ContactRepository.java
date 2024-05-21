package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
	
	List<Contact> findAllByUserId(UUID id);
}
