package com.lolmatch.chat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolmatch.chat.dto.UserDTO;
import com.lolmatch.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpUserRegisterQueueReceiver {
	
	private final String queueName = "user-register-queue-chat";
	
	private final UserService userService;
	
	@Bean
	public Queue myQueue() {
		return new Queue(queueName, true);
	}
	
	@RabbitListener(queues = queueName)
	public void listen(String in) throws JsonProcessingException {
		log.info("Message from user-register-queue-chat: " + in);
		ObjectMapper objectMapper = new ObjectMapper();
		UserDTO userDTO = objectMapper.readValue(in, UserDTO.class);
		
		userService.saveUserFromDTO(userDTO);
	}
}
