package com.lolmatch.calendar.validator;

import com.lolmatch.calendar.audit.ApplicationAuditAware;
import com.lolmatch.calendar.dto.request.MeetingScheduleRequest;
import com.lolmatch.calendar.model.MeetingDictionaryHelper;
import com.lolmatch.calendar.model.MeetingEntity;
import com.lolmatch.calendar.model.dictionary.MeetingIntervalDictionary;
import com.lolmatch.calendar.model.status.MeetingStatus;
import com.lolmatch.calendar.repositories.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.lolmatch.calendar.model.status.MeetingStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingValidator {

    private static final int MINIMUM_MEETING_DURATION_TIME_IN_MINUTES = 5;
    private final ApplicationAuditAware audit;
    private final MeetingDictionaryHelper meetingDictionaryHelper;
    private final MeetingRepository meetingRepository;

    public MeetingStatus isValid(MeetingScheduleRequest meeting) {

        if (Objects.isNull(meeting.getName())
                || Objects.isNull(meeting.getStartDate())
                || Objects.isNull(meeting.getEndDate())
                || Objects.isNull(meeting.getParticipants())
                || meeting.getParticipants().isEmpty()
                || meeting.getStartDate().isAfter(meeting.getEndDate())
                || (Duration.between(meeting.getStartDate(), meeting.getEndDate())
                .compareTo(Duration.ofMinutes(MINIMUM_MEETING_DURATION_TIME_IN_MINUTES)) < 0)
        ) {
            log.warn("Incorrect input data");
            return INVALID;
        }

        final UUID currentUser = audit.getCurrentAuditor()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User has to be authenticated!"));

        if (meeting.getParticipants().contains(currentUser)) {
            log.warn("User cannot schedule meeting with himself.");
            return INVALID;
        }

        List<MeetingEntity> existingMeetings = meetingRepository.findMeetingsByCreatorWithinTimeframe(meeting.getStartDate(), meeting.getEndDate(), currentUser);

        if (!existingMeetings.isEmpty()) {
            log.warn("The user has already scheduled a meeting within the proposed time frame.");
            return ALREADY_OCCUPIED_DATE;
        }

        return SUCCESS;
    }

    public MeetingStatus isValid(String interval) {
        if (!meetingDictionaryHelper.getWholeIntervalDictionaryAsSet().contains(interval)) {
            return INVALID;
        }

        return SUCCESS;
    }

    public MeetingStatus isValid(Integer year, Integer month, Integer day) {
        if (year == null || month == null) {
            return INVALID;
        }

        if (month < 1 || month > 12) {
            return INVALID;
        }

        if (day != null) {
            try {
                LocalDate.of(year, month, day);
            } catch (Exception e) {
                return INVALID;
            }
        }

        return SUCCESS;
    }
}
