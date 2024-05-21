package com.lolmatch.chat.interceptors;

import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.dto.UserStatusChangeDTO;
import com.lolmatch.chat.util.UserStatusChangeEvent;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ChangeUserStatusChannelInterceptor implements ChannelInterceptor {
	
	private final ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
			applicationEventPublisher.publishEvent(new UserStatusChangeEvent(this, new UserStatusChangeDTO(accessor.getUser().getName(), UserStatusChangeDTO.StatusType.ACTIVE)));
		} else if (accessor != null && StompCommand.DISCONNECT.equals(accessor.getCommand())){
			applicationEventPublisher.publishEvent(new UserStatusChangeEvent(this, new UserStatusChangeDTO(accessor.getUser().getName(), UserStatusChangeDTO.StatusType.INACTIVE)));
		}
		return message;
	}
}
