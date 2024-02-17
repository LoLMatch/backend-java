package com.lolmatch.chat.util;

import com.lolmatch.chat.dto.UserStatusChangeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class ChangeUserStatusChannelInterceptor implements ChannelInterceptor {
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
			System.out.println("ACTIVE");
			applicationEventPublisher.publishEvent(new UserStatusChangeEvent(this, new UserStatusChangeDTO(accessor.getUser().getName(), UserStatusChangeDTO.StatusType.ACTIVE)));
		} else if (accessor != null && StompCommand.DISCONNECT.equals(accessor.getCommand())){
			System.out.println("INACTIVE");
			applicationEventPublisher.publishEvent(new UserStatusChangeEvent(this, new UserStatusChangeDTO(accessor.getUser().getName(), UserStatusChangeDTO.StatusType.INACTIVE)));
		}
		return message;
	}
}
