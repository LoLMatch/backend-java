package com.lolmatch.chat.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contacts")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.EAGER)
	private User contact;
	
}
