package com.example.demo.controller;

import com.example.demo.entity.Meeting;
import com.example.demo.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings(@RequestParam(required = false) String description,
                                                        @RequestParam(required = false) String responsiblePerson,
                                                        @RequestParam(required = false) String category,
                                                        @RequestParam(required = false) String type,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                        @RequestParam(required = false) Integer minAttendees) {

        List<Meeting> meetings = meetingService.getAllMeetings(description, responsiblePerson, category, type, fromDate, toDate, minAttendees);
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable String id) {
        Optional<Meeting> meeting = meetingService.getMeetingById(id);
        return meeting.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) {
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeeting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable String id,
                                              @RequestParam String requester) {
        meetingService.deleteMeeting(id, requester);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attendees")
    public ResponseEntity<Void> addAttendee(@PathVariable String id,
                                            @RequestParam String attendee,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time) {
        meetingService.addAttendee(id, attendee, time);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/attendees/{attendee}")
    public ResponseEntity<Void> removeAttendee(@PathVariable String id, @PathVariable String attendee) {
        meetingService.removeAttendee(id, attendee);
        return ResponseEntity.ok().build();
    }
}
