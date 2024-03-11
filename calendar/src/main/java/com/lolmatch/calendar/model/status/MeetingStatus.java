package com.lolmatch.calendar.model.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetingStatus {

    SUCCESS(true),
    ALREADY_OCCUPIED_DATE(false),
    CANNOT_FIND_ALL_PARTICIPANTS(false),
    INVALID(false);

    private final boolean isCorrect;
}
