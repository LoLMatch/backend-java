package com.lolmatch.teams.team.dto;

import com.lolmatch.teams.team.Rank;
import com.lolmatch.teams.team.Team;
import com.lolmatch.teams.user.dto.UserDTO;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TeamDTO(
		UUID id,
		UUID leaderId,
		String name,
		String description,
		Set<UserDTO> members,
		boolean isPublic,
		String teamCountry,
		Rank minimalRank,
		BigDecimal teamWinRate) {
	
	public Team toDto(){
		return new Team(
				id,
				name,
				description,
				members.stream()
						.map(UserDTO::toUser)
						.collect(Collectors.toSet()),
				isPublic,
				minimalRank,
				leaderId,
				teamCountry,
				null,
				teamWinRate
		);
	}
}
