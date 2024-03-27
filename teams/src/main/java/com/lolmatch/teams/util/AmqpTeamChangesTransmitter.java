package com.lolmatch.teams.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolmatch.teams.team.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpTeamChangesTransmitter {
	
	private final RabbitTemplate rabbitTemplate;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final String userChangeTeamQueue = "user-change-team-queue";
	
	private final String teamChangeQueue = "team-change-queue";
	
	@Bean
	private Queue userChangeTeamQueue() {
		return new Queue(userChangeTeamQueue, true);
	}
	
	@Bean
	private Queue teamChangeQueue() {
		return new Queue(teamChangeQueue, true);
	}
	
	public void transmitTeamChange(Team team, TeamChangeEnum type) {
		TeamChangeMessage msg = new TeamChangeMessage(team.getId(), team.getLeaderId(), team.getName(), type);
		try {
			rabbitTemplate.convertAndSend(teamChangeQueue, objectMapper.writeValueAsString(msg));
			log.info("message on team-change-queue: " + msg);
		} catch (JsonProcessingException e) {
			log.warn("error while processing msg to json: " + msg);
			throw new RuntimeException(e);
		}
	}
	
	public void transmitUserChange(UUID teamId, UUID userId, UserChangeEnum type) {
		UserChangeMessage msg = new UserChangeMessage(userId, teamId, type);
		try {
			rabbitTemplate.convertAndSend(userChangeTeamQueue, objectMapper.writeValueAsString(msg));
			log.info("Message sent on user-changed-team-queue: " + msg);
		} catch (JsonProcessingException e) {
			log.warn("error while processing msg to json: " + msg);
			throw new RuntimeException(e);
		}
	}
	
	public enum UserChangeEnum {JOIN, LEAVE}
	
	public enum TeamChangeEnum {CREATE, UPDATE, DELETE}
	
	private record UserChangeMessage(UUID userId, UUID teamId, UserChangeEnum type) {
	}
	
	private record TeamChangeMessage(UUID teamId, UUID leaderId, String name, TeamChangeEnum type) {
	}
}
