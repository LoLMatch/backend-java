package com.lolmatch.calendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "meetings", schema = "meetings")
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class MeetingEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<MeetingParticipant> participants = new HashSet<>();

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


