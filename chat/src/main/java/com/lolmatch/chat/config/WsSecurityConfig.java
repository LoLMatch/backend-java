package com.lolmatch.chat.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WsSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
	// FIXME - websocket security is configured in deprecated way, because new method cannot disable csrf
	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
				.simpTypeMatchers(SimpMessageType.CONNECT).permitAll()
				.nullDestMatcher().authenticated()
				.anyMessage().authenticated();
	}
	
	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
}

