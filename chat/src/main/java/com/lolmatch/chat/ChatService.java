package com.lolmatch.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@EnableDiscoveryClient
@SpringBootApplication
public class ChatService {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ChatService.class, args);

		if (context.getEnvironment().matchesProfiles("local", "docker")){
			context.getBean(TestDataInitializer.class).initUsers();
		}
	}
}