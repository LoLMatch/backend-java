package com.lolmatch.teams.team.dto;

import com.lolmatch.teams.team.Rank;
import com.lolmatch.teams.user.dto.UserDTO;

import java.util.Set;
import java.util.UUID;

public record TeamDTO(
		UUID id,
		UUID leaderId,
		String name,
		String description,
		Set<UserDTO> members,
		boolean isPublic,
		String teamCountry,
		Rank minimalRank) {
}
