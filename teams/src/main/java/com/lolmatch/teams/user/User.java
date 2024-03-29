package com.lolmatch.teams.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lolmatch.teams.team.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id", referencedColumnName = "id")
	private Team team;
	
	private BigDecimal winRate;
	
	public User(UUID id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		User user = (User) o;
		
		return id.equals(user.id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
