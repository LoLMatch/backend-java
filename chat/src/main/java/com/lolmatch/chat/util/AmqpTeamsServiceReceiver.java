package com.lolmatch.chat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolmatch.chat.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpTeamsServiceReceiver {
	private final String userChangeTeamQueue = "user-change-team-queue";
	private final String teamChangeQueue = "team-change-queue";
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private final GroupService groupService;
	
	@Bean
	private Queue userChangeTeamQueue() {
		return new Queue(userChangeTeamQueue, true);
	}
	
	@Bean
	private Queue teamChangeQueue() {
		return new Queue(teamChangeQueue, true);
	}
	
	@RabbitListener(queues = userChangeTeamQueue)
	public void listenToUserChange(String in) throws JsonProcessingException {
		log.info("Message from user-change-team-queue: " + in);
		UserChangeMessage msg = objectMapper.readValue(in, UserChangeMessage.class);
		
		switch (msg.type) {
			case JOIN -> groupService.addUserToGroup(msg.teamId, msg.userId);
			case LEAVE -> groupService.deleteUserFromGroup(msg.userId);
			default -> {
				log.warn("Wrong msg type on user-change-team-queue: " + msg);
				throw new IllegalArgumentException("Wrong type on msg: " + msg);
			}
		}
	}
	
	@RabbitListener(queues = teamChangeQueue)
	public void listenToTeamChange(String in) throws JsonProcessingException {
		log.info("Message from team-change-queue: " + in);
		TeamChangeMessage msg = objectMapper.readValue(in, TeamChangeMessage.class);
		
		switch (msg.type) {
			case CREATE -> groupService.saveGroup(msg.teamId, msg.leaderId, msg.name);
			case UPDATE -> groupService.editGroup(msg.teamId, msg.name);
			case DELETE -> groupService.deleteGroup(msg.teamId);
			default -> {
				log.warn("Wrong msg type on user-change-team-queue: " + msg);
				throw new IllegalArgumentException("Wrong type on msg: " + msg);
			}
		}
	}
	
	private record UserChangeMessage(UUID userId, UUID teamId, UserChangeEnum type) {
	}
	
	private record TeamChangeMessage(UUID teamId, UUID leaderId, String name, TeamChangeEnum type) {
	}
	
	public enum UserChangeEnum {JOIN, LEAVE}
	
	public enum TeamChangeEnum {CREATE, UPDATE, DELETE}
}
