package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.dto.UserDTO;
import com.lolmatch.chat.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	private final Environment environment;
	
	public User getUserByUUID(UUID id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new EntityNotFoundException("No user with id: " + id + ", has been found!");
		} else {
			return user.get();
		}
	}
	
	public void saveUserFromDTO(UserDTO userDTO) {
		User user = new User();
		user.setId(userDTO.id());
		user.setUsername(userDTO.username());
		user.setProfilePictureId(0);
		
		userRepository.save(user);
	}
	
	
	public int updateProfilePicture(UUID id) {
		// var instance = eurekaClient.getNextServerFromEureka("recommender", false);
		
		String url;
		if (environment.matchesProfiles("local")) {
			url = "http://localhost:5000";
		} else {
			url = "http://recommender:5000";
		}
		
		RestClient client = RestClient.builder()
				.baseUrl(url)
				.build();
		
		ProfileDto result;
		try {
			result = client.get()
					.uri(uriBuilder -> uriBuilder
							.path("/profile")
							.queryParam("summoner_id", id)
							.build())
					.retrieve()
					.toEntity(ProfileDto.class)
					.getBody();
		} catch (Exception e) {
			log.warn("Cannot fetch profile picture id");
			return 0;
		}
		
		if (result == null || !result.id.equals(id)) {
			throw new RuntimeException("Wrong ppid fetch");
		}
		
		userRepository.updateProfilePicture(result.icon_id, id);
		
		return result.icon_id;
	}
	
	
	public void updateUser(UUID id, UserDTO dto) {
		userRepository.updateProfilePicture(dto.profilePictureId(), id);
	}
	
	private record ProfileDto(UUID id, int icon_id){}
}
