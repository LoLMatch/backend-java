package com.lolmatch.calendar.dto.response;

import com.lolmatch.calendar.errors.MeetingScheduleError;
import com.lolmatch.calendar.model.status.MeetingStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class MeetingScheduleResponse {
    String status;
    String description;
    @Nullable
    UUID meetingId;

    public MeetingScheduleResponse(final MeetingStatus status, final UUID meetingId) {
        this.status = status.name();
        this.meetingId = meetingId;
        switch (status) {
            case SUCCESS -> this.description = "Success: meeting has been scheduled";
            default -> throw new MeetingScheduleError("Unexpected error, hit @Sereda");
        }
    }

    public MeetingScheduleResponse(final MeetingStatus status) {
        this.status = status.name();
        this.meetingId = null;
        switch (status) {
            case INVALID -> this.description = "Error: Invalid request, verify input data";
            case CANNOT_FIND_ALL_PARTICIPANTS -> this.description = "Error: Not all given participants could be found";
            case ALREADY_OCCUPIED_DATE -> this.description = "Error: meeting has not been scheduled";
            default -> throw new MeetingScheduleError("Unexpected error, hit @Sereda");
        }
    }

    public MeetingScheduleResponse(MeetingStatus status, String description) {
        this.status = status.name();
        this.description = description;
        this.meetingId = null;
    }
}
