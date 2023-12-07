package com.lolmatch.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user")
public class User {
	
	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "username")
	private String username;
}
