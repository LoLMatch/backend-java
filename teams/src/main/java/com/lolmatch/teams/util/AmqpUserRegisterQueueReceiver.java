package com.lolmatch.teams.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolmatch.teams.user.UserService;
import com.lolmatch.teams.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpUserRegisterQueueReceiver {
	
	private final String queueName = "user-register-queue-teams";
	
	private final UserService userService;
	
	@Bean
	public Queue userRegisterQueue() {
		return new Queue(queueName, true);
	}
	
	@RabbitListener(queues = queueName)
	public void listen(String in) throws JsonProcessingException {
		log.info("Message from user-register-queue-teams: " + in);
		ObjectMapper objectMapper = new ObjectMapper();
		UserDTO userDTO = objectMapper.readValue(in, UserDTO.class);
		
		userService.saveUser(userDTO);
	}
}
