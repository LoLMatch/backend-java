package com.lolmatch.chat.util;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

// interceptor made to print messages - only for test purposes, delete later
public class TestChannelInterceptor implements ChannelInterceptor {
	@Override
	public Message<?> preSend(@NonNull Message<?> message,@NonNull MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		System.out.println(message.getPayload().toString());
		System.out.println(message.getHeaders().toString());
		StompCommand command = accessor.getCommand();
		return message;
	}
}
