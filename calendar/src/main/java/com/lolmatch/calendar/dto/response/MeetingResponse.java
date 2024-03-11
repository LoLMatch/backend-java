package com.lolmatch.calendar.dto.response;

import com.lolmatch.calendar.model.MeetingEntity;
import com.lolmatch.calendar.model.status.MeetingStatus;
import com.lolmatch.calendar.model.status.PresentStatus;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class MeetingResponse {

    String status;
    MeetingEntity meeting;

    public MeetingResponse(Enum<?> status) {
        this.status = status.name();
        this.meeting = null;
    }

    public MeetingResponse(MeetingStatus status, MeetingEntity meeting) {
        this.status = status.name();
        this.meeting = meeting;
    }
}
