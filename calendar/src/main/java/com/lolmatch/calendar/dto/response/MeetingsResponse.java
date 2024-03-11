package com.lolmatch.calendar.dto.response;

import com.lolmatch.calendar.model.MeetingEntity;
import com.lolmatch.calendar.model.status.MeetingStatus;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class MeetingsResponse {
    String status;
    String interval;
    List<MeetingEntity> usersMeetings;

    public MeetingsResponse(MeetingStatus meetingStatus, String interval) {
        this.status = String.valueOf(meetingStatus);
        this.interval = interval;
        this.usersMeetings = List.of();
    }

    public MeetingsResponse(MeetingStatus status, String interval, List<MeetingEntity> meetings) {
        this.status = status.name();
        this.interval = interval;
        this.usersMeetings = meetings;
    }
}
