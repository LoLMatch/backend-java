package com.lolmatch.calendar.mapper;

import com.lolmatch.calendar.audit.ApplicationAuditAware;
import com.lolmatch.calendar.dto.request.MeetingScheduleRequest;
import com.lolmatch.calendar.errors.MeetingScheduleError;
import com.lolmatch.calendar.model.MeetingEntity;
import com.lolmatch.calendar.model.MeetingParticipant;
import com.lolmatch.calendar.model.UserEntity;
import com.lolmatch.calendar.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MeetingMapper {

    private final UserRepository userRepository;
    private final ApplicationAuditAware audit;

    public MeetingEntity mapOf(MeetingScheduleRequest meeting) {
        final List<UserEntity> users = userRepository.findAllById(meeting.getParticipants());
        if (users.size() != meeting.getParticipants().size()) {
            throw new MeetingScheduleError("Error: Not all given participants could be found");
        }

        final UUID currentUser = audit.getCurrentAuditor()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User has to be authenticated!"));
        final UserEntity creator = userRepository.findById(currentUser)
                .orElseThrow(() -> new IllegalStateException("User has to be authenticated!"));

        Set<MeetingParticipant> participants = users.stream()
                .map(user -> {
                    MeetingParticipant participant = new MeetingParticipant();
                    participant.setUser(user);
                    return participant;
                })
                .collect(Collectors.toSet());

        final MeetingEntity meetingEntity = MeetingEntity.builder()
                .name(meeting.getName())
                .creator(creator)
                .startTime(meeting.getStartDate())
                .endTime(meeting.getEndDate())
                .participants(participants)
                .build();

        participants.forEach(participant -> participant.setMeeting(meetingEntity));
        return meetingEntity;
    }
}
