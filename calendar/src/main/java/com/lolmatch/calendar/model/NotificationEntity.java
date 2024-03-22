package com.lolmatch.calendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "notifications", schema = "meetings")
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;
    private UUID userId;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead; // todo - to nie jest obsłużone, jak bd czas to można ew dodać
}
