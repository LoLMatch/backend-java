package com.lolmatch.teams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TeamsService {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TeamsService.class, args);
		
		if( context.getEnvironment().matchesProfiles("dev","local")){
			// if local or dev then init data to db
			context.getBean(TestDataInitializer.class).initUsers();
		}
	}
}