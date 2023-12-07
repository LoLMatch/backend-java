package com.lolmatch.entity;

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
	@GeneratedValue( strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "username")
	private String username;
	
	@ManyToMany(mappedBy = "users")
	private Set<Group> groups;
	
	@OneToMany(mappedBy = "senderId")
	private Set<Message> sentMessages;
	
	@OneToMany(mappedBy = "receiverId")
	private Set<Message> receivedMessages;
}
