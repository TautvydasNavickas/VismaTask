package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Meeting;
import com.example.demo.entity.Type;
import com.example.demo.service.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) {
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        if (createdMeeting != null) {
            return ResponseEntity.ok(createdMeeting);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{meetingName}")
    public ResponseEntity<String> deleteMeeting(@PathVariable String meetingName,
                                                @RequestParam String responsiblePerson) {
        meetingService.deleteMeeting(meetingName, responsiblePerson);
        return ResponseEntity.ok("Meeting deleted successfully");
    }

    @PostMapping("/{meetingName}/attendees")
    public ResponseEntity<String> addAttendee(@PathVariable String meetingName,
                                              @RequestParam String attendee,
                                              @RequestParam LocalDate time) {
        meetingService.addAttendee(meetingName, attendee, time);
        return ResponseEntity.ok("Attendee added successfully");
    }

    @DeleteMapping("/{meetingName}/attendees")
    public ResponseEntity<String> removeAttendee(@PathVariable String meetingName,
                                                 @RequestParam String attendee) {
        meetingService.removeAttendee(meetingName, attendee);
        return ResponseEntity.ok("Attendee removed successfully");
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getMeetings(@RequestParam(required = false) String description,
                                                     @RequestParam(required = false) String responsiblePerson,
                                                     @RequestParam(required = false) Category category,
                                                     @RequestParam(required = false) Type type,
                                                     @RequestParam(required = false) LocalDate startDate,
                                                     @RequestParam(required = false) LocalDate endDate,
                                                     @RequestParam(required = false, defaultValue = "0") int minAttendees) {
        List<Meeting> filteredMeetings = meetingService.getFilteredMeetings(description, responsiblePerson,
                category, type, startDate, endDate, minAttendees);
        return ResponseEntity.ok(filteredMeetings);
    }
}
