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
@Table(name = "group")
public class Group {
	
	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany
	@JoinTable(
			name = "user_group_t",
			joinColumns = @JoinColumn(name = "group_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<User> users;
	
	@OneToMany(mappedBy = "groupReceiverId")
	private Set<Message> groupMessages;
}
