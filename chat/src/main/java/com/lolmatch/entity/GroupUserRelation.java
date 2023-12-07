package com.lolmatch.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_group_t")
public class GroupUserRelation {
	
	@Id
	@GeneratedValue( strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "user_id")
	private UUID userId;
	
	@Column(name = "group_id")
	private UUID groupId;
}
