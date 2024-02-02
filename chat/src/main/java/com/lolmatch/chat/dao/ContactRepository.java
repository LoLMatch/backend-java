package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
	
	public List<Contact> findAllByUser(User user);

}
