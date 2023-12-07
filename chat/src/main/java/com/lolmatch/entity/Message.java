package com.lolmatch.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "message")
public class Message {
	
	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	
	@Column(name = "created_at")
	private Timestamp createdAt;
	
	@Column(name = "read_at")
	private Timestamp readAt;
	
	@Column(name = "sender_id")
	private UUID senderId;
	
	@Column(name = "receiver_id")
	private UUID receiverId;
	
	@Column(name = "group_receiver_id")
	private UUID groupReceiverId;
	
	@Column(name = "parent_message_id")
	private UUID parentMessageId;
	
}
