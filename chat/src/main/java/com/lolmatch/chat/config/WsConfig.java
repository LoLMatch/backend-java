package com.lolmatch.chat.config;

import com.lolmatch.chat.interceptors.AuthChannelInterceptor;
import com.lolmatch.chat.interceptors.LogMessagesChannelInterceptor;
import com.lolmatch.chat.interceptors.ChangeUserStatusChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WsConfig implements WebSocketMessageBrokerConfigurer {
	
	private final AuthChannelInterceptor authChannelInterceptor;
	private final ChangeUserStatusChannelInterceptor changeUserStatusChannelInterceptor;
	private final LogMessagesChannelInterceptor logMessagesChannelInterceptor;
	private final Environment environment;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOrigins("*");
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic/chat");
		registry.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// log all messages only on dev and local
		if ( environment.matchesProfiles("local", "dev")) {
			registration.interceptors(logMessagesChannelInterceptor);
		}
		registration.interceptors(authChannelInterceptor);
		registration.interceptors(changeUserStatusChannelInterceptor);
	}
	
	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.interceptors(logMessagesChannelInterceptor);
	}
	
}
