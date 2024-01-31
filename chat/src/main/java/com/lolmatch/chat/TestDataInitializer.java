package com.lolmatch.chat;

import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {
	
	private final UserRepository userRepository;
	
	public void initUsers(){
		User user = new User();
		user.setId(UUID.fromString("fd0a67ca-1fe7-4759-854b-4ba0a1ac818e"));
		user.setUsername("bob");
		
		User testUser = userRepository.save(user);
	}
}
