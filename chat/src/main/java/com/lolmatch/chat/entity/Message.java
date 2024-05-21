package com.lolmatch.chat.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lolmatch.chat.dto.MessageDTO;
import com.lolmatch.chat.dto.ReadStatusDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages", indexes = {
		@Index(name = "idx_message_sender_id", columnList = "sender_id, recipient_id")
})
public class Message {
	
	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	
	@Column(name = "created_at", nullable = false)
	private Timestamp createdAt;
	
	@Column(name = "read_at")
	private Timestamp readAt;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "sender_id")
	private User sender;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "recipient_id")
	private User recipient;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "group_recipient_id")
	private Group groupRecipient;
	
	@OneToOne
	@JoinColumn(name = "parent_message_id", referencedColumnName = "id")
	private Message parentMessage;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
	private List<ReadStatus> readStatuses;
	
	public MessageDTO toDto(String action){
		List<ReadStatusDTO> statusList;
		UUID recipientId;
		UUID parentId;
		if ( recipient == null){
			// grupowa
			if (readStatuses != null){
				statusList = readStatuses.stream().map(ReadStatus::toDto).toList();
			} else {
				statusList = Collections.emptyList();
			}
			 recipientId = groupRecipient.getId();
		} else {
			// zwykła
			statusList = Collections.emptyList();
			recipientId = recipient.getId();
		}
		if ( parentMessage == null){
			parentId = null;
		} else {
			parentId = parentMessage.getId();
		}
		return new MessageDTO( action, id, content,createdAt,readAt,sender.getId(),recipientId, parentId, statusList);
	}
}
