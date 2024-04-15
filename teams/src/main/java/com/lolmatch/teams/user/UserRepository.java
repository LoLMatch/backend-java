package com.lolmatch.teams.user;

import com.lolmatch.teams.user.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	@Query(value = "SELECT new com.lolmatch.teams.user.dto.UserDTO(u.id, u.username, u.profilePictureId, u.winRate, u.team.id) FROM User u where u.id = ?1")
	Optional<UserDTO> findUserById(UUID id);
}
