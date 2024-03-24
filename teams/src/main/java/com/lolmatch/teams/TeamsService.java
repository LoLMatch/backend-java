package com.lolmatch.teams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamsService {
	
	public static void main(String[] args) {
		TestDataInitializer dataInitializer = SpringApplication.run(TeamsService.class, args).getBean(TestDataInitializer.class);
		dataInitializer.initUsers();
	}
}