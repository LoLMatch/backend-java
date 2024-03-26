package com.lolmatch.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class ReadStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private UUID userId;
	
	private Timestamp readAt;
	
	@ManyToOne
	@JoinColumn(name = "group_id")
	private Message message;
}
