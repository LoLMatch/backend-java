package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	@Modifying
	@Query(value = "UPDATE User u SET u.profilePictureId = ?1 WHERE u.id = ?2")
	void updateProfilePicture(int profilePictureId, UUID id);
}
