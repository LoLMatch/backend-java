package com.lolmatch.chat.entity;

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
	
	@ManyToMany(mappedBy = "users")
	private Set<Group> groups;
	
	@OneToMany(mappedBy = "sender")
	private Set<Message> sentMessages;
	
	@OneToMany(mappedBy = "recipient")
	private Set<Message> receivedMessages;
}
