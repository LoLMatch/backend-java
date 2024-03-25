package com.lolmatch.calendar.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolmatch.calendar.errors.NotificationError;
import com.lolmatch.calendar.model.NotificationEntity;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.lolmatch.calendar.model.status.PresentStatus.FOUND;
import static com.lolmatch.calendar.model.status.PresentStatus.NOT_FOUND;

@Value
public class NotificationsResponse {
    String status;
    Integer currentPage;
    Integer totalPages;
    Long totalItems;
    Integer pageSize;
    List<NotificationResponse> notifications;

    public NotificationsResponse(Page<NotificationEntity> page, ObjectMapper objectMapper) {
        this.status = page.hasContent() ? FOUND.name() : NOT_FOUND.name();
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.pageSize = page.getSize();

        this.notifications = page.getContent().stream().map(notificationEntity -> {
            try {
                return objectMapper.readValue(notificationEntity.getMessage(), NotificationResponse.class);
            } catch (Exception e) {
                throw new NotificationError("Failed to deserialize notification message");
            }
        }).toList();
    }
}
