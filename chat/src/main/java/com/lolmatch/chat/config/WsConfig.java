package com.lolmatch.chat.config;

import com.lolmatch.chat.util.TestChannelInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WsConfig implements WebSocketMessageBrokerConfigurer {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;
	
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
		registration.interceptors( new TestChannelInterceptor());
		registration.interceptors( new AuthCHannelInterceptor());
	}
	
	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.interceptors(new TestChannelInterceptor());
	}
	
	private class AuthCHannelInterceptor implements ChannelInterceptor{
		
		private final JwtDecoder jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
		@Override
		public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
			StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
			if ( accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
				String authHeader = accessor.getFirstNativeHeader("Authorization");
				if (authHeader == null || !authHeader.startsWith("Bearer" + " ")){
					throw new AccessDeniedException("Bad auth header: " + authHeader);
				}
				String token = authHeader.substring("Bearer".length() + 1);
				Jwt jwt;
				JwtAuthenticationToken authenticationToken;
				try{
					jwt = jwtDecoder.decode(token);
					authenticationToken = new JwtAuthenticationToken(jwt);
					authenticationToken.setAuthenticated(true);
					accessor.setUser(authenticationToken);
				} catch (JwtException e){
					throw new AccessDeniedException("No auth header");
				}
			}
			return message;
		}
	}
}
