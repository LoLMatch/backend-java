package com.lolmatch.teams.team;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lolmatch.teams.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teams")
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@JsonBackReference
	@OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
	private Set<User> members;
	
	private boolean isPublic;
	
	private Rank minimalRank;
	
	private UUID leaderId;
	
	private String teamCountry;
	
	private String password;
	
	private BigDecimal teamWinRate;
}
