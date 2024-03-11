package com.lolmatch.calendar.service;

import com.lolmatch.calendar.audit.ApplicationAuditAware;
import com.lolmatch.calendar.dto.request.MeetingScheduleRequest;
import com.lolmatch.calendar.dto.response.DeleteResponse;
import com.lolmatch.calendar.dto.response.MeetingResponse;
import com.lolmatch.calendar.dto.response.MeetingScheduleResponse;
import com.lolmatch.calendar.dto.response.MeetingsResponse;
import com.lolmatch.calendar.errors.MeetingScheduleError;
import com.lolmatch.calendar.mapper.MeetingMapper;
import com.lolmatch.calendar.model.MeetingEntity;
import com.lolmatch.calendar.model.dictionary.MeetingIntervalDictionary;
import com.lolmatch.calendar.model.status.MeetingStatus;
import com.lolmatch.calendar.repositories.MeetingRepository;
import com.lolmatch.calendar.validator.MeetingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.lolmatch.calendar.model.status.MeetingStatus.CANNOT_FIND_ALL_PARTICIPANTS;
import static com.lolmatch.calendar.model.status.MeetingStatus.SUCCESS;
import static com.lolmatch.calendar.model.status.PresentStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingValidator meetingValidator;
    private final MeetingMapper meetingMapper;

    private final ApplicationAuditAware audit;

    public MeetingScheduleResponse createMeeting(MeetingScheduleRequest meeting) {
        final MeetingStatus meetingStatus = meetingValidator.isValid(meeting);
        if (!meetingStatus.isCorrect()) {
            return new MeetingScheduleResponse(meetingStatus);
        }

        try {
            final UUID createdMeeting = meetingRepository.save(meetingMapper.mapOf(meeting)).getId();
            return new MeetingScheduleResponse(SUCCESS, createdMeeting);
        } catch (MeetingScheduleError error) {
            return new MeetingScheduleResponse(CANNOT_FIND_ALL_PARTICIPANTS, error.getMessage());
        } catch (Exception e) {
            throw new MeetingScheduleError("Unexpected error, scheduling meeting failed, check database connection " + e);
        }
    }

    public MeetingsResponse getAllMeetingsByPeriod(String interval) {

        final MeetingStatus meetingStatus = meetingValidator.isValid(interval);
        if (!meetingStatus.isCorrect()) {
            return new MeetingsResponse(meetingStatus, interval);
        }

        final UUID currentUser = audit.getCurrentAuditor()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User has to be authenticated!"));

        LocalDateTime startTime;
        LocalDateTime endTime;

        switch (MeetingIntervalDictionary.valueOf(interval)) {
            case DAY -> {
                startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
                endTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
                log.info(startTime.toString());
                log.info(endTime.toString());
            }
            case WEEK -> {
                startTime = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0);
                endTime = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59);
            }
            case MONTH -> {
                startTime = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
                endTime = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
            }
            default -> throw new IllegalArgumentException("Invalid interval");
        }

        final List<MeetingEntity> meetings = meetingRepository.findAllMeetingsByPeriodAndCreator(startTime, endTime, currentUser);

        return new MeetingsResponse(SUCCESS, interval, meetings);
    }

    public DeleteResponse deleteMeeting(UUID uuid) {
        final UUID currentUser = audit.getCurrentAuditor()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User has to be authenticated!"));

        Optional<MeetingEntity> meeting = meetingRepository.findById(uuid);

        if (meeting.isEmpty()) {
            return new DeleteResponse(NOT_FOUND);
        }

        if (!meeting.get().getCreator().getId().equals(currentUser)) {
            throw new AccessDeniedException("Lack of permissions to delete this meeting.");
        }

        meetingRepository.deleteById(uuid);
        return new DeleteResponse(SUCCESS);
    }

    public MeetingResponse getMeeting(UUID uuid) {
        final UUID currentUser = audit.getCurrentAuditor()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User has to be authenticated!"));

        Optional<MeetingEntity> meeting = meetingRepository.findByIdWithParticipants(uuid);

        if (meeting.isEmpty()) {
            return new MeetingResponse(NOT_FOUND);
        }

        if (!meeting.get().getCreator().getId().equals(currentUser)) {
            throw new AccessDeniedException("Lack of permissions to delete this meeting.");
        }

        return new MeetingResponse(SUCCESS, meeting.get());
    }
}
