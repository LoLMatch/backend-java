package com.lolmatch.teams.team.dto;

import java.util.Optional;
import java.util.UUID;

public record AddUserToTeamRequest(UUID userId, UUID teamId, Optional<String> password) {
}
