package com.lolmatch.chat.dao;

import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.dto.UnreadMessagesDTO;
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
	// TODO - pozmieniać wszędzie gdzie jest read-only na DTO poprawne
	@Modifying
	@Transactional
	@Query(value = "UPDATE Message m SET m.readAt = ?1 WHERE m.sender.id = ?2 AND m.recipient.id = ?3 AND m.readAt IS null")
	void setReadTimestamp(Timestamp readAt, UUID senderId, UUID recipientId); // GIT
	
	@Query(value =
	"""
	SELECT new com.lolmatch.chat.dto.MessageDTO( "MESSAGE", m.id, m.content, m.createdAt, m.readAt, m.sender.id, m.recipient.id, m.parentMessage.id)\s
		FROM Message m WHERE (m.sender.id = ?1 AND m.recipient.id = ?2) OR (m.sender.id = ?2 AND m.recipient.id = ?1) ORDER BY m.createdAt DESC
	""", countQuery = "SELECT count(m.id) FROM Message m WHERE (m.sender.id = ?1 AND m.recipient.id = ?2) OR (m.sender.id = ?2 AND m.recipient.id = ?1)"
	)
	Page<MessageDTO> getMessagesBetweenUsersPaged(UUID senderId, UUID recipientId, Pageable pageable);
	
	Page<Message> findAllByGroupRecipientIdOrderByCreatedAtDesc(UUID groupRecipientId, Pageable pageable);
	
	@Query(value = "SELECT m FROM Message m WHERE m.groupRecipient.id = ?1 ORDER BY m.createdAt DESC LIMIT 1")
	Optional<Message> getLastMessageInGroup(UUID groupId);
	
	@Query(value = "SELECT COUNT(m) FROM Message m LEFT JOIN ReadStatus r ON r.message = m AND r.userId = ?1 WHERE m.groupRecipient.id = ?2 AND r.id IS NULL")
	Long countAllUnreadByUserIdAndGroupId(UUID userId, UUID groupId); // GIT
	
	@Query(value = "SELECT m FROM Message m LEFT JOIN FETCH ReadStatus r ON r.message = m AND r.userId = ?1 WHERE m.groupRecipient.id = ?2 AND r.id IS NULL")
	List<Message> findAllGroupMessagesUnreadByUserIdAndGroupId(UUID userId, UUID groupId);
	
	@Query(value = "SELECT new com.lolmatch.chat.dto.UnreadMessagesDTO(COUNT(m) , m.sender.id) as userId FROM Message m WHERE m.recipient.id = ?1 AND m.readAt IS NULL GROUP BY m.sender.id")
	List<UnreadMessagesDTO> countUnreadMessagesForContactsOfUser(UUID userId); // GIT
	
	@Query(value =
   """
		   SELECT new com.lolmatch.chat.dto.MessageDTO( "MESSAGE", m.id, m.content, m.createdAt, m.readAt, m.sender.id, m.recipient.id)
		   FROM Message m WHERE (m.sender.id = ?1 OR m.recipient.id = ?1)
		   AND (m.sender.id IN (SELECT c.user.id FROM Contact c WHERE c.user.id = ?1) OR m.recipient.id IN (SELECT c.user.id FROM Contact c WHERE c.user.id = ?1))
		   AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE ((m2.sender = m.sender AND m2.recipient = m.recipient) OR (m2.sender = m.recipient AND m2.recipient = m.sender))
		   ORDER BY m.createdAt DESC)
		   """)
	// TODO - sprawdzić czy tutaj będzie dodatkowo pobierać userów, jak tak to dodać JOIN FETCH Users u
	List<MessageDTO> getLastMessagesBetweenUserAndContact(UUID userId);
}
