package com.lolmatch.calendar.dto.request;

import jakarta.annotation.Nullable;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Value
public class MeetingScheduleRequest {

    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<UUID> participants;
    @Nullable
    Boolean shouldNotify;
}
