package com.lolmatch.teams.user;

import com.lolmatch.teams.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	// TODO - add win rate and profile picture fetching
	private final UserRepository userRepository;
	
	@Transactional
	public void saveUser(UserDTO dto) {
		User user = dto.toUser();
		userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public UserDTO getUserById(UUID id) {
		// TODO - sprawdzić czy działa
		return userRepository.findUserById(id).orElseThrow();
		//return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id: " + id + ", has been found.")).toDto();
	}
}
