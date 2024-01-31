package com.lolmatch.chat.util;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;


@Component
public class AmqpUserRegisterQueueReceiver {
	
	private final String queueName = "user-register-queue";
	
	@Bean
	public Queue myQueue() {
		return new Queue(queueName, false);
	}
	
	@RabbitListener(queues = queueName)
	public void listen(String in) {
		System.out.println("Message read from myQueue : " + in);
	}
}
