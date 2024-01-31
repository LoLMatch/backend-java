package com.lolmatch.chat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolmatch.chat.dto.UserDTO;
import com.lolmatch.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;


@Component
@RequiredArgsConstructor
public class AmqpUserRegisterQueueReceiver {
	
	private final String queueName = "user-register-queue";
	
	private final UserService userService;
	
	@Bean
	public Queue myQueue() {
		return new Queue(queueName, false);
	}
	
	@RabbitListener(queues = queueName)
	public void listen(String in) throws JsonProcessingException {
		// TODO - change println to proper logging
		System.out.println("Message read from myQueue : " + in);
		ObjectMapper objectMapper = new ObjectMapper();
		UserDTO userDTO = objectMapper.readValue(in, UserDTO.class);
		
		userService.saveUserFromDTO(userDTO);
	}
}
