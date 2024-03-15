package com.lolmatch.chat.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.nio.charset.StandardCharsets;

// interceptor made to print messages - only for test purposes, delete later
@Slf4j
public class TestChannelInterceptor implements ChannelInterceptor {
	@Override
	public Message<?> preSend(@NonNull Message<?> message,@NonNull MessageChannel channel) {
		byte[] payload = (byte[]) message.getPayload();
		log.info(new String(payload, StandardCharsets.UTF_8));
		log.info(message.getHeaders().toString());
		return message;
	}
}
