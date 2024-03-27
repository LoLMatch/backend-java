package com.lolmatch.teams.user.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UserDTO(UUID id, String username, BigDecimal winRate) {
}
