package com.lolmatch.teams.user;

import com.lolmatch.teams.team.TeamRepository;
import com.lolmatch.teams.team.dto.TeamDTO;
import com.lolmatch.teams.user.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	// TODO - add win rate and profile picture fetching
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	
	@Transactional
	public void saveUser(UserDTO dto) {
		User user = dto.toUser();
		userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public UserDTO getUserById(UUID id) {
		return userRepository.findUserById(id).orElseThrow(() -> new EntityNotFoundException("No user with id: " + id + ", has been found."));
	}
	
	@Transactional
	public TeamDTO getUserTeamById(UUID id){
		return teamRepository.findTeamByUser(id).orElseThrow(() -> new EntityNotFoundException("No team with user with id: " + id + ", has been found.")).toDto();
	}
}
