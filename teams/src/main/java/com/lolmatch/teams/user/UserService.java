package com.lolmatch.teams.user;

import com.lolmatch.teams.user.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	// TODO - dodać pobieranie winrate z pythona w jakiś sposób, raczej po HTTP
	private final UserRepository userRepository;
	
	public static UserDTO mapUserToUserDTO(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getWinRate());
	}
	
	public void saveUser(UserDTO dto) {
		User user = new User();
		user.setId(dto.id());
		user.setUsername(dto.username());
		
		userRepository.save(user);
	}
	
	public User getUserById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id: " + id + ", has been found."));
	}
}
