package com.lolmatch.chat.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
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
	@JoinColumn(name = "sender_id", nullable = false)
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
	
	@OneToMany(mappedBy = "message")
	private List<ReadStatus> readStatuses;
}
