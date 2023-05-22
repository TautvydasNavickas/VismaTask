package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Meeting;
import com.example.demo.entity.Type;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    private List<Meeting> meetings;
    private ObjectMapper objectMapper;
    private final File file;

    public MeetingService() {
        this.objectMapper = new ObjectMapper();
        this.file = new File(getClass().getClassLoader().getResource("data/meetings.json").getFile());
        loadMeetingsFromFile();
    }

    private void loadMeetingsFromFile() {
        try {
            meetings = objectMapper.readValue(file, new TypeReference<List<Meeting>>() {});
        } catch (IOException e) {
            meetings = new ArrayList<>();
        }
    }

    private void saveMeetingData() {
        try {
            objectMapper.writeValue(file, meetings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Meeting createMeeting(Meeting meeting) {
        meetings.add(meeting);
        saveMeetingData();
        return meeting;
    }

    public void deleteMeeting(String meetingName, String responsiblePerson) {
        meetings.removeIf(meeting -> meeting.getName().equals(meetingName) &&
                meeting.getResponsiblePerson().equals(responsiblePerson));
        saveMeetingData();
    }

    public void addAttendee(String meetingName, String attendee, LocalDate time) {
        Meeting meeting = getMeetingByName(meetingName);
        if (meeting != null && !meeting.getResponsiblePerson().equals(attendee)) {
            if (!meeting.getAttendees().containsKey(time)) {
                meeting.getAttendees().put(time, attendee);
                saveMeetingData();
            } else {
                System.out.println("Warning: Person is already in a meeting at the specified time.");
            }
        }
    }

    public void removeAttendee(String meetingName, String attendee) {
        Meeting meeting = getMeetingByName(meetingName);
        if (meeting != null && !meeting.getResponsiblePerson().equals(attendee)) {
            meeting.getAttendees().entrySet().removeIf(entry -> entry.getValue().equals(attendee));
            saveMeetingData();
        }
    }

    public List<Meeting> getFilteredMeetings(String description, String responsiblePerson,Category category,
                                             Type type, LocalDate startDate, LocalDate endDate, int minAttendees) {
        return meetings.stream()
                .filter(meeting -> description == null || meeting.getDescription().toLowerCase().contains(description.toLowerCase()))
                .filter(meeting -> responsiblePerson == null || meeting.getResponsiblePerson().equals(responsiblePerson))
                .filter(meeting -> category == null || meeting.getCategory() == category)
                .filter(meeting -> type == null || meeting.getType() == type)
                .filter(meeting -> startDate == null || meeting.getStartDate().isAfter(startDate.minusDays(1)))
                .filter(meeting -> endDate == null || meeting.getEndDate().isBefore(endDate.plusDays(1)))
                .filter(meeting -> meeting.getAttendees().size() >= minAttendees)
                .collect(Collectors.toList());
    }

    private Meeting getMeetingByName(String meetingName) {
        return meetings.stream()
                .filter(meeting -> meeting.getName().equals(meetingName))
                .findFirst()
                .orElse(null);
    }
}
