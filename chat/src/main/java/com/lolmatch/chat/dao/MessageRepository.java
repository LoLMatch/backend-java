package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE Message m SET m.readAt = ?1 WHERE m.sender.id = ?2 AND m.recipient.id = ?3 AND m.readAt IS null")
	void setReadTimestamp(Timestamp readAt, UUID senderId, UUID recipientId);
	
	@Query(value = "SELECT m FROM Message m WHERE (m.sender.id = ?1 AND m.recipient.id = ?2) OR (m.sender.id = ?2 AND m.recipient.id = ?1) ORDER BY m.createdAt DESC",
			countQuery = "SELECT count(m.id) FROM Message m WHERE (m.sender.id = ?1 AND m.recipient.id = ?2) OR (m.sender.id = ?2 AND m.recipient.id = ?1)"
	)
	Page<Message> getMessagesBetweenUsersPaged(UUID senderId, UUID recipientId, Pageable pageable);
	
	Page<Message> findAllByGroupRecipientIdOrderByCreatedAtDesc(UUID groupRecipientId, Pageable pageable);
	
	@Query(value = "SELECT m FROM Message m WHERE m.groupRecipient.id = ?1 ORDER BY m.createdAt DESC LIMIT 1")
	Optional<Message> getLastMessageInGroup(UUID groupId);
	
	@Query(value = "SELECT COUNT(m) FROM Message m LEFT JOIN ReadStatus r ON r.message = m AND r.userId = ?1 WHERE m.groupRecipient.id = ?2 AND r.id IS NULL")
	Long countAllUnreadByUserIdAndGroupId(UUID userId, UUID groupId);
	
	@Query(value = "SELECT m FROM Message m LEFT JOIN ReadStatus r ON r.message = m AND r.userId = ?1 WHERE m.groupRecipient.id = ?2 AND r.id IS NULL")
	List<Message> findAllGroupMessagesUnreadByUserIdAndGroupId(UUID userId, UUID groupId);
	
	@Query(value = "SELECT COUNT(m) as unreadMessages, m.sender.id as userId FROM Message m WHERE m.recipient.id = ?1 AND m.readAt IS NULL GROUP BY m.sender.id")
	List<Object[]> countUnreadForContactsOfUser(UUID userId);
	
	@Query(value = "SELECT m FROM Message m " +
			"WHERE (m.sender.id = ?1 OR m.recipient.id = ?1) " +
			"AND (m.sender.id IN (SELECT c.contactId FROM Contact c WHERE c.user.id = ?1) OR m.recipient.id IN (SELECT c.contactId FROM Contact c WHERE c.user.id = ?1)) " +
			"AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE ((m2.sender = m.sender AND m2.recipient = m.recipient) OR (m2.sender = m.recipient AND m2.recipient = m.sender)) " +
			"ORDER BY m.createdAt DESC)")
	List<Message> getLastMessagesBetweenUserAndContact(UUID userId);
}
