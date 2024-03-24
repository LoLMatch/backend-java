package com.lolmatch.teams;

import com.lolmatch.teams.user.User;
import com.lolmatch.teams.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {
	
	private final UserRepository userRepository;
	
	public void initUsers() {
		User user = new User();
		user.setId(UUID.fromString("fd0a67ca-1fe7-4759-854b-4ba0a1ac818e"));
		user.setUsername("bob");
		user.setWinRate(BigDecimal.valueOf(51.00));
		User bob = userRepository.save(user);
		
		user.setId(UUID.fromString("c8973806-df29-4ae7-8bab-79a2c52b7193"));
		user.setUsername("ash");
		user.setWinRate(BigDecimal.valueOf(48.50));
		User ash = userRepository.save(user);
		
		user.setId(UUID.fromString("21d8e0d9-2545-4404-925e-e245032ec5cc"));
		user.setUsername("rob");
		user.setWinRate(BigDecimal.valueOf(52.69));
		User rob = userRepository.save(user);
		
		user.setId(UUID.fromString("b4b70f3a-7aa4-4d53-8020-e466d1e1a019"));
		user.setUsername("user");
		user.setWinRate(BigDecimal.valueOf(46.70));
		User user1 = userRepository.save(user);
	}
}
