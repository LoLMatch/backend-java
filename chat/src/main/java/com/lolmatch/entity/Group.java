package com.lolmatch.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "group")
public class Group {
	
	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "name")
	private String name;
}
