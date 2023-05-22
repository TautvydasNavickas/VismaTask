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
    Meeting saveMeeting(Meeting meeting);
    Meeting deleteMeeting(String meetingId);
    Meeting addPersonToMeeting(String meetingId, String person, LocalDateTime time);
    Meeting removePersonFromMeeting(String meetingId, String person);
    List<Meeting> findAllMeetings();
}


