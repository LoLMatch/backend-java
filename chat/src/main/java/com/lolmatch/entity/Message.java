package com.lolmatch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "message")
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
	@JoinColumn(name = "sender_id", nullable = false)
	private User senderId;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private User receiverId;
	
	@ManyToOne
	@JoinColumn(name = "group_receiver_id")
	private Group groupReceiverId;
	
	@OneToOne
	@JoinColumn(name = "parent_message_id", referencedColumnName = "id")
	private Message parentMessageId;
	
}
