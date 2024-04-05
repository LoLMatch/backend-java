package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.dto.UserDTO;
import com.lolmatch.chat.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	public User getUserByUUID(UUID id){
		Optional<User> user =  userRepository.findById(id);
		if ( user.isEmpty()){
			throw new EntityNotFoundException("No user with id: " + id + ", has been found!");
		} else {
			return user.get();
		}
	}
	
	public void saveUserFromDTO(UserDTO userDTO){
		User user = new User();
		user.setId(userDTO.id());
		user.setUsername(userDTO.username());
		
		userRepository.save(user);
	}
}
