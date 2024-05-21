package com.lolmatch.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "users")
public class User {
	@Id
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "profile_picture_id")
	private int profilePictureId;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "group_id")
	private Group group;
	
	@JsonBackReference
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Contact> contacts;
	
	@JsonBackReference
	@OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
	private Set<Contact> backContacts;
	
	@JsonBackReference
	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
	private Set<Message> sentMessages;
	
	@JsonBackReference
	@OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
	private Set<Message> receivedMessages;
}
