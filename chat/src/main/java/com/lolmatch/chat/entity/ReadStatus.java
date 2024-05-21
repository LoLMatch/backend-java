package com.lolmatch.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lolmatch.chat.dto.ReadStatusDTO;
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
	@JsonBackReference
	@JoinColumn(name = "group_id")
	private Message message;
	
	public ReadStatusDTO toDto(){
		return new ReadStatusDTO(id, userId,readAt,message.getId());
	}
}
