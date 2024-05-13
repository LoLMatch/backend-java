package com.lolmatch.calendar.controller;

import com.lolmatch.calendar.dto.response.NotificationsResponse;
import com.lolmatch.calendar.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NotificationsResponse> getUnreadNotifications(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UUID userId = UUID.fromString(principal.getName());
        return ResponseEntity.ok(notificationService.getAllNotifications(userId, page, size));
    }
}
