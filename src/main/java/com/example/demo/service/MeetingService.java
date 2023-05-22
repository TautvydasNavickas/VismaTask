package com.example.demo.service;


import com.example.demo.entity.Meeting;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingService {
    private List<Meeting> meetings;
    private static final String JSON_FILE_PATH = "meetings.json";

    @PostConstruct
    public void initialize() {
        meetings = loadMeetingsFromJson();
    }

    public Meeting createMeeting(Meeting meeting) {
        meetings.add(meeting);
        saveMeetingsToJson();
        return meeting;
    }

    public List<Meeting> getAllMeetings() {
        return meetings;
    }

    private void saveMeetingsToJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(JSON_FILE_PATH), meetings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Meeting> loadMeetingsFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(JSON_FILE_PATH);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<List<Meeting>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

