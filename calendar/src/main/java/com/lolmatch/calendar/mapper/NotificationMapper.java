package com.lolmatch.calendar.mapper;

import com.lolmatch.calendar.audit.ApplicationAuditAware;
import com.lolmatch.calendar.dto.request.MeetingScheduleRequest;
import com.lolmatch.calendar.dto.response.NotificationResponse;
import com.lolmatch.calendar.model.UserEntity;
import com.lolmatch.calendar.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final ApplicationAuditAware audit;
    private final UserRepository userRepository;

    public NotificationResponse mapOf(MeetingScheduleRequest meetingScheduleRequest, UUID meetingUUID) {
        final UUID currentUser = audit.getCurrentAuditor()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User has to be authenticated!"));
        final UserEntity creator = userRepository.findById(currentUser).orElseThrow();
        final String creatorName = creator.getUsername();

        return new NotificationResponse(
                meetingScheduleRequest.getName(),
                creatorName,
                meetingUUID,
                LocalDateTime.now().toString()
        );
    }
}
