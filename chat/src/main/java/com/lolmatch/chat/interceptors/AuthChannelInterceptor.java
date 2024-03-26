package com.lolmatch.chat.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;
	
	@Override
	public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
		// TODO - czy to jest optymalne żeby to tworzyć za każdym razem?
		final JwtDecoder jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
		
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		// verify Auth header when joining
		if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authHeader = accessor.getFirstNativeHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer" + " ")) {
				throw new AccessDeniedException("Bad auth header: " + authHeader);
			}
			Jwt jwt;
			String token = authHeader.substring("Bearer".length() + 1);
			JwtAuthenticationToken authenticationToken;
			try {
				jwt = jwtDecoder.decode(token);
				authenticationToken = new JwtAuthenticationToken(jwt);
				authenticationToken.setAuthenticated(true);
				accessor.setUser(authenticationToken);
			} catch (JwtException e) {
				throw new AccessDeniedException("No auth header");
			}
		}
		// verify if user tries to subscribe someone else channel
		if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
			String destinationHeader = accessor.getFirstNativeHeader("destination");
			if (destinationHeader == null || !destinationHeader.startsWith("/topic/chat/")){
				throw new RuntimeException("Bad destination header on subscribe frame: " + destinationHeader);
			}
			Principal principal = accessor.getUser();
			if (principal == null){
				throw new AccessDeniedException("Bad auth on subscribe message");
			}
			if (!principal.getName().equals(destinationHeader.substring(12))){
				throw new AccessDeniedException("Cannot subscribe to another user channel: " + principal.getName());
			}
		}
		return message;
	}
}