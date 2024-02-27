package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
	
	int countAllBySenderIdAndRecipientAndReadAtIsNull(UUID senderId, User recipient);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE Message m SET m.readAt = ?1 WHERE m.sender.id = ?2 AND m.recipient.id = ?3 AND m.readAt IS null")
	void setReadTimestamp(Timestamp readAt, UUID senderId, UUID recipientId);
	
	@Query(value = "SELECT m FROM Message m WHERE (m.sender.id = ?1 AND m.recipient.id = ?2) OR (m.sender.id = ?2 AND m.recipient.id = ?1) ORDER BY m.createdAt DESC",
			countQuery = "SELECT count(m.id) FROM Message m WHERE (m.sender.id = ?1 AND m.recipient.id = ?2) OR (m.sender.id = ?2 AND m.recipient.id = ?1)"
	)
	Page<Message> getMessagesBetweenUsersPaged(UUID senderId, UUID recipientId, Pageable pageable);
	
	@Query(value = "SELECT m FROM Message m WHERE (m.sender.id = ?1 AND m.recipient.id = ?2) OR (m.sender.id = ?2 AND m.recipient.id = ?1) ORDER BY m.createdAt DESC LIMIT 1")
	Optional<Message> getLastMessageBetweenUsers(UUID senderId, UUID recipientId);
}
