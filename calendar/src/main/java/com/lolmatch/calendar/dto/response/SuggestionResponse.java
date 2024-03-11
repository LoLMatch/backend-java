package com.lolmatch.calendar.dto.response;

import com.lolmatch.calendar.model.UserEntity;
import lombok.Data;

import java.util.List;

import static com.lolmatch.calendar.model.status.PresentStatus.FOUND;
import static com.lolmatch.calendar.model.status.PresentStatus.NOT_FOUND;

@Data
public class SuggestionResponse {
    String status;
    Integer quantity;
    List<UserEntity> suggestedUsers;

    public SuggestionResponse(List<UserEntity> suggestedUsers) {
        this.suggestedUsers = suggestedUsers;
        if (suggestedUsers.isEmpty()) {
            this.status = NOT_FOUND.name();
        } else {
            this.status = FOUND.name();
        }
        this.quantity = suggestedUsers.size();
    }
}
