package com.lolmatch.calendar.controller;

import com.lolmatch.calendar.dto.response.SuggestionResponse;
import com.lolmatch.calendar.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/calendar/suggestions")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<SuggestionResponse> getSuggestions(@RequestParam(required = false) String snippet) {
        log.info("Getting suggestions");
        return ResponseEntity.ok(userService.getSuggestions(Optional.ofNullable(snippet)));
    }
}
