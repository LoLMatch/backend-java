package com.lolmatch.calendar.controller;

import com.lolmatch.calendar.dto.request.MeetingScheduleRequest;
import com.lolmatch.calendar.dto.response.DeleteResponse;
import com.lolmatch.calendar.dto.response.MeetingResponse;
import com.lolmatch.calendar.dto.response.MeetingScheduleResponse;
import com.lolmatch.calendar.dto.response.MeetingsResponse;
import com.lolmatch.calendar.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MeetingScheduleResponse> createMeeting(@RequestBody MeetingScheduleRequest meeting) {
        log.info("Create meeting " + meeting.toString());
        return ResponseEntity.ok(meetingService.createMeeting(meeting));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MeetingsResponse> getAllMeetings(@RequestParam String interval) {
        log.info("Get all meetings with interval " + interval);
        return ResponseEntity.ok(meetingService.getAllMeetingsByPeriod(Strings.toRootUpperCase(interval)));
    }

    @GetMapping("/all/bydate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MeetingsResponse> getMeetingsByDate(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day) {
        log.info("Get meetings by date with year: " + year + ", month: " + month + ", day: " + day);
        return ResponseEntity.ok(meetingService.getMeetingsByDate(year, month, day));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MeetingResponse> getMeeting(@RequestParam UUID uuid) {
        log.info("Get specified meeting with uuid: " + uuid);
        return ResponseEntity.ok(meetingService.getMeeting(uuid));
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DeleteResponse> deleteMeeting(@RequestParam UUID uuid) {
        log.info("Delete a meeting: " + uuid);
        return ResponseEntity.ok(meetingService.deleteMeeting(uuid));
    }
}