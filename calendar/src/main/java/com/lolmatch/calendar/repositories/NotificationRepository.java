package com.lolmatch.calendar.repositories;

import com.lolmatch.calendar.model.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    Page<NotificationEntity> findAllByUserId(UUID userId, Pageable pageable);
}
