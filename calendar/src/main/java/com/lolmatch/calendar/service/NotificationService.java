package com.lolmatch.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lolmatch.calendar.dto.request.MeetingScheduleRequest;
import com.lolmatch.calendar.dto.response.NotificationsResponse;
import com.lolmatch.calendar.errors.NotificationError;
import com.lolmatch.calendar.mapper.NotificationMapper;
import com.lolmatch.calendar.model.NotificationEntity;
import com.lolmatch.calendar.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void notifyUser(UUID userId, MeetingScheduleRequest meetingDetails, UUID meetingUUID) {

        log.info("Notifying user = [{}] about meeting = [{}]", userId, meetingDetails.getName());

        final NotificationEntity notification = createNotification(userId, meetingDetails, meetingUUID);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(userId.toString(), "/calendar/notifications", notification.getMessage());
    }

    private NotificationEntity createNotification(UUID userId, MeetingScheduleRequest meetingDetails, UUID meetingUUID) {
        var notificationResponse = notificationMapper.mapOf(meetingDetails, meetingUUID);
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(notificationResponse);
        } catch (JsonProcessingException e) {
            throw new NotificationError("Error serializing notification to JSON" + e);
        }

        final NotificationEntity notification = new NotificationEntity();
        notification.setUserId(userId);
        notification.setMessage(jsonMessage);
        notification.setRead(false);
        return notification;
    }

    public NotificationsResponse getAllNotifications(UUID userId, int page, int quantity) {
        return new NotificationsResponse(notificationRepository.findAllByUserId(userId, PageRequest.of(page, quantity)), objectMapper);
    }


}
