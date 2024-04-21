package com.lolmatch.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        welcome();
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @SuppressWarnings("squid:S106")
    private static void welcome() {
        final String welcomeUri = "welcome/welcome.txt";
        try (InputStream inputStream = ApiGatewayApplication.class.getClassLoader().getResourceAsStream(welcomeUri)) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String welcomeMessage = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                    System.out.println(welcomeMessage);
                }
            } else {
                log.warn("Welcome file not found. Starting application without welcome message.");
            }
        } catch (IOException e) {
            log.error("Error reading welcome message :( \n");
        }
    }
}