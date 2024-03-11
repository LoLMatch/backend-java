package com.lolmatch.calendar.repositories;

import com.lolmatch.calendar.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    List<UserEntity> findByUsernameStartingWith(String snippet);
}
