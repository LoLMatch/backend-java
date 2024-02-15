package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.dto.UserDTO;
import com.lolmatch.chat.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	public String getUsernameByUUID(UUID id) {
		return null;
	}
	
	public User getUserByUUID(UUID id){
		Optional<User> user =  userRepository.findById(id);
		if ( user.isEmpty()){
			throw new EntityNotFoundException("No user with id: " + id + ", has been found!");
		} else {
			return user.get();
		}
	}
	
	public User saveUserFromDTO(UserDTO userDTO){
		User user = new User();
		user.setId(userDTO.getId());
		user.setUsername(userDTO.getUsername());
		
		return userRepository.save(user);
	}
}
