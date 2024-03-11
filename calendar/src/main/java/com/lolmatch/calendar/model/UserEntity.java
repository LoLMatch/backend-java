package com.lolmatch.calendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calendar_users", schema = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    private UUID id;
    private String username;

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    @JsonIgnore
    private LocalDateTime lastModified;
}
