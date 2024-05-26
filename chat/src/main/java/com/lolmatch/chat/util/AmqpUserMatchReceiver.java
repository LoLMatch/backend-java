package com.lolmatch.chat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolmatch.chat.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmqpUserMatchReceiver {
	
	private final ContactService contactService;
	private final String queueName = "user-matches-queue";
	
	@Bean
	public Queue userMatchesQueue() {
		return new Queue(queueName, true);
	}
	
	@RabbitListener(queues = queueName)
	public void listen(String in) throws JsonProcessingException {
		log.info("Message from user-matches-queue: " + in);
		ObjectMapper objectMapper = new ObjectMapper();
		userMatch match = objectMapper.readValue(in, userMatch.class);
		
		contactService.saveContact(match.firstUserId, match.secondUserId);
	}
	
	private record userMatch(UUID firstUserId, UUID secondUserId){}
}
