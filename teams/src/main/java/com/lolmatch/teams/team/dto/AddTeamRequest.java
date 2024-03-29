package com.lolmatch.teams.team.dto;

import com.lolmatch.teams.team.Rank;
import com.lolmatch.teams.util.validation.PublicAndPasswordValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@PublicAndPasswordValid
public record AddTeamRequest(
		@NotBlank(message = "Team name is mandatory")
		String name,
		@NotNull
		boolean isPublic,
		String password,
		@NotNull(message = "leader id must be present")
		UUID leaderId,
		@NotBlank(message = "Team description is mandatory")
		String description,
		@NotBlank(message = "Team country is mandatory")
		String teamCountry,
		@NotNull(message = "Team must have a minimal rank")
		Rank minimalRank
) {
}