package com.lolmatch.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ChatService {

	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ChatService.class, args);

		if (context.getEnvironment().matchesProfiles("local", "dev")){
			context.getBean(TestDataInitializer.class).initUsers();
		}
	}
}