package com.lolmatch.calendar.dto.response;

import lombok.Value;

@Value
public class DeleteResponse {
    String status;

    public DeleteResponse(Enum<?> status) {
        this.status = status.name();
    }
}
