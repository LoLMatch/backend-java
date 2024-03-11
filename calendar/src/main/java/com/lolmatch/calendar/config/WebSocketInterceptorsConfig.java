package com.lolmatch.calendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketInterceptorsConfig {

    @Bean
    public AuthChannelInterceptor authChannelInterceptor() {
        return new AuthChannelInterceptor();
    }
}
