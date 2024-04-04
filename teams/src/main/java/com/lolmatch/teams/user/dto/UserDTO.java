package com.lolmatch.teams.user.dto;

import com.lolmatch.teams.user.User;

import java.math.BigDecimal;
import java.util.UUID;

public record UserDTO(UUID id, String username, BigDecimal winRate) {
	public User toUser(){
		return User.builder()
				.id(id)
				.username(username)
				.winRate(winRate)
				.build();
	}
}
