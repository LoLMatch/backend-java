package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.Group;
import com.lolmatch.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

	Optional<Group> findByUsersContains(User user);
}
