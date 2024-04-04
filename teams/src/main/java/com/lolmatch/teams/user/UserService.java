package com.lolmatch.teams.user;

import com.lolmatch.teams.user.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	// TODO - add win rate and profile picture fetching
	private final UserRepository userRepository;
	
	public void saveUser(UserDTO dto) {
		User user = dto.toUser();
		userRepository.save(user);
	}
	
	public UserDTO getUserById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id: " + id + ", has been found.")).toDto();
	}
}
