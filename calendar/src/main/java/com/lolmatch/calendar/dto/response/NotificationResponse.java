package com.lolmatch.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class NotificationResponse {
    String name;
    String creator;
    UUID meetingUUID;
    String timestamp;

    @JsonCreator
    public NotificationResponse(
            @JsonProperty("name") String name,
            @JsonProperty("creator") String creator,
            @JsonProperty("meetingUUID") UUID meetingUUID,
            @JsonProperty("timestamp") String timestamp) {
        this.name = name;
        this.creator = creator;
        this.meetingUUID = meetingUUID;
        this.timestamp = timestamp;
    }
}
