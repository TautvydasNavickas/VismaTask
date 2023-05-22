package com.example.demo.service;

import com.example.demo.entity.Meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MeetingService {

    private final ObjectMapper objectMapper;
    private final String filePath = "src/main/resources/meetings.json";
    private final Map<String, Meeting> meetings = new ConcurrentHashMap<>();

    public MeetingService() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        loadMeetingsFromFile();
    }

    public List<Meeting> getAllMeetings(String description, String responsiblePerson, String category, String type, LocalDate fromDate, LocalDate toDate, Integer minAttendees) {
        Stream<Meeting> meetingStream = meetings.values().stream();

        if (description != null) {
            meetingStream = meetingStream.filter(meeting -> meeting.getDescription().contains(description));
        }

        if (responsiblePerson != null) {
            meetingStream = meetingStream.filter(meeting -> meeting.getResponsiblePerson().equals(responsiblePerson));
        }

        if (category != null) {
            meetingStream = meetingStream.filter(meeting -> meeting.getCategory().name().equalsIgnoreCase(category));
        }

        if (type != null) {
            meetingStream = meetingStream.filter(meeting -> meeting.getType().name().equalsIgnoreCase(type));
        }

        if (fromDate != null) {
            meetingStream = meetingStream.filter(meeting -> !meeting.getStartDate().isBefore(fromDate));
        }

        if (toDate != null) {
            meetingStream = meetingStream.filter(meeting -> !meeting.getEndDate().isAfter(toDate));
        }

        if (minAttendees != null) {
            meetingStream = meetingStream.filter(meeting -> meeting.getAttendees().size() >= minAttendees);
        }

        return meetingStream.collect(Collectors.toList());
    }

    public Optional<Meeting> getMeetingById(String id) {
        return Optional.ofNullable(meetings.get(id));
    }

    public Meeting createMeeting(Meeting meeting) {
        String id = UUID.randomUUID().toString();
        meeting.setId(id);
        meetings.put(id, meeting);
        saveMeetingsToFile();
        return meeting;
    }

    public void deleteMeeting(String id, String requester) {
        Meeting meeting = meetings.get(id);
        if (meeting != null) {
            if (meeting.getResponsiblePerson().equals(requester)) {
                meetings.remove(id);
                saveMeetingsToFile();
            } else {
                throw new IllegalArgumentException("Only the person responsible can delete the meeting.");
            }
        } else {
            throw new IllegalArgumentException("Meeting not found.");
        }
    }

    public void addAttendee(String id, String attendee, LocalDate time) {
        Meeting meeting = meetings.get(id);
        if (meeting != null) {
            if (!meeting.getAttendees().containsKey(attendee) && !isAttendeeBusy(attendee, time)) {
                meeting.getAttendees().put(attendee, time);
                saveMeetingsToFile();
            } else {
                throw new IllegalArgumentException("Attendee is already in a conflicting meeting.");
            }
        } else {
            throw new IllegalArgumentException("Meeting not found.");
        }
    }

    public void removeAttendee(String id, String attendee) {
        Meeting meeting = meetings.get(id);
        if (meeting != null) {
            if (!meeting.getResponsiblePerson().equals(attendee)) {
                meeting.getAttendees().remove(attendee);
                saveMeetingsToFile();
            } else {
                throw new IllegalArgumentException("The responsible person cannot be removed.");
            }
        } else {
            throw new IllegalArgumentException("Meeting not found.");
        }
    }

    private boolean isAttendeeBusy(String attendee, LocalDate date) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getAttendees().containsKey(attendee))
                .anyMatch(meeting -> meeting.getAttendees().get(attendee).isEqual(date));
    }

    private void loadMeetingsFromFile() {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Meeting[] meetingsArray = objectMapper.readValue(file, Meeting[].class);
                for (Meeting meeting : meetingsArray) {
                    meetings.put(meeting.getId(), meeting);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMeetingsToFile() {
        try {
            File file = new File(filePath);
            objectMapper.writeValue(file, meetings.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}