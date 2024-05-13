package com.lolmatch.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ChatService {

    public static void main(String[] args) {
        TestDataInitializer dataInitializer = SpringApplication.run(ChatService.class, args).getBean(TestDataInitializer.class);
        dataInitializer.initUsers();
    }
}