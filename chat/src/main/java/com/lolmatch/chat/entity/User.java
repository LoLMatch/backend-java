package com.lolmatch.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {
	
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "username")
	private String username;
	
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private Set<Group> groups;
	
	@JsonBackReference
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Contact> contacts;
	
	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
	private Set<Message> sentMessages;
	
	@OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
	private Set<Message> receivedMessages;
}
