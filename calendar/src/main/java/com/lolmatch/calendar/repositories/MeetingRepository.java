package com.lolmatch.calendar.repositories;

import com.lolmatch.calendar.model.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<MeetingEntity, UUID> {
    @Query("SELECT m FROM MeetingEntity m JOIN FETCH m.participants WHERE m.startTime >= :startTime AND m.endTime <= :endTime AND m.creator.id = :creatorId")
    List<MeetingEntity> findAllMeetingsByPeriodAndCreator(LocalDateTime startTime, LocalDateTime endTime, UUID creatorId);

    @Query("SELECT m FROM MeetingEntity m WHERE m.creator.id = :creatorId AND ((m.startTime < :endTime AND m.startTime >= :startTime) OR (m.endTime > :startTime AND m.endTime <= :endTime))")
    List<MeetingEntity> findMeetingsByCreatorWithinTimeframe(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("creatorId") UUID creatorId);

    @Query("SELECT m FROM MeetingEntity m LEFT JOIN FETCH m.participants WHERE m.id = :id")
    Optional<MeetingEntity> findByIdWithParticipants(@Param("id") UUID id);
}
