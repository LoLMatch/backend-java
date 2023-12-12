package com.lolmatch.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
public class WsSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
	// FIXME - websocket security jest zrobione w stary sposób bo w nowym nie można wyłączyć CSRF
	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
				.simpDestMatchers("/user/**").authenticated();
		// TODO - skonfigurować to w odpowiedni sposób
	}
	
	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
}

