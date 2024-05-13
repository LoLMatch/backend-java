package com.lolmatch.teams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@EnableDiscoveryClient
@SpringBootApplication
public class TeamsService {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TeamsService.class, args);
		
		if( context.getEnvironment().matchesProfiles("docker","local")){
			// if local or docker then init data to db
			context.getBean(TestDataInitializer.class).initUsers();
		}
	}
}