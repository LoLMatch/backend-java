package com.lolmatch.teams.user.dto;

import com.lolmatch.teams.user.User;

import java.math.BigDecimal;
import java.util.UUID;

public record UserDTO(UUID id, String username, int profilePictureId, BigDecimal winRate, UUID teamId) {
	public User toUser(){
		return User.builder()
				.id(id)
				.username(username)
				.profilePictureId(profilePictureId)
				.winRate(winRate)
				.build();
	}
}
