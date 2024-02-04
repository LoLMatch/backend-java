package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
	
	int countAllBySenderAndRecipientAndReadAtIsNull(User sender, User recipient);
	
	
	List<Message> findAllBySenderAndRecipientAndReadAtIsNull(User sender, User recipient);
}
