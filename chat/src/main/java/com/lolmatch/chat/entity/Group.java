package com.lolmatch.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group {
	@Id
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "name")
	private String name;
	
	@JsonBackReference
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	private Set<User> users;
	
	@JsonBackReference
	@OneToMany(mappedBy = "groupRecipient", fetch = FetchType.LAZY)
	private Set<Message> groupMessages;

}
