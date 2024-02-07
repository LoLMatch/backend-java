package com.lolmatch.chat.util;

import com.lolmatch.chat.dto.UserStatusChangeDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class UserStatusChangeEvent extends ApplicationEvent {
	
	private UserStatusChangeDTO statusChangeDTO;
	
	public UserStatusChangeEvent(Object source, UserStatusChangeDTO statusDTO) {
		super(source);
		statusChangeDTO = statusDTO;
	}
}
