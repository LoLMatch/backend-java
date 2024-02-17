package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
	
	int countAllBySenderIdAndRecipientAndReadAtIsNull(UUID senderId, User recipient);
	
	long countAllBySenderIdAndRecipientId(UUID senderId, UUID recipientId);
	
	List<Message> findAllBySenderIdAndRecipientIdAndReadAtIsNull(UUID senderId, UUID recipientId);
}
