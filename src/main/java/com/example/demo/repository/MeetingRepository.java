package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.Meeting;
import com.example.demo.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository {
    Meeting createMeeting(Meeting meeting);

    Meeting deleteMeeting(String meetingId, String responsiblePerson);

    Meeting addPersonToMeeting(String meetingId, String person, LocalDateTime time);

    Meeting removePersonFromMeeting(String meetingId, String person);

    List<Meeting> listMeetings(String description, String responsiblePerson, Category category,
                               Type type, LocalDate fromDate, LocalDate toDate, int minAttendees);
}

