package com.example.demo;

import com.example.demo.entity.Meeting;
import com.example.demo.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingServiceTest {

    @Mock
    private MeetingService meetingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMeetings() {
        Meeting meeting1 = new Meeting("1", "Meeting 1", "John", LocalDate.now(), LocalDate.now(), new HashMap<>());
        Meeting meeting2 = new Meeting("2", "Meeting 2", "Alice", LocalDate.now(), LocalDate.now(), new HashMap<>());
        Meeting meeting3 = new Meeting("3", "Meeting 3", "John", LocalDate.now(), LocalDate.now(), new HashMap<>());
        List<Meeting> meetings = Arrays.asList(meeting1, meeting2, meeting3);

        when(meetingService.getAllMeetings(anyString(), anyString(), anyString(), anyString(), any(), any(), any()))
                .thenReturn(meetings);

        List<Meeting> result = meetingService.getAllMeetings(null, null, null, null, null, null, null);

        verify(meetingService).getAllMeetings(null, null, null, null, null, null, null);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(meeting1));
        assertTrue(result.contains(meeting2));
        assertTrue(result.contains(meeting3));
    }

    @Test
    void testGetMeetingById() {

        Meeting meeting = new Meeting("1", "Meeting 1", "John", LocalDate.now(), LocalDate.now(), new HashMap<>());


        when(meetingService.getMeetingById("1")).thenReturn(Optional.of(meeting));

        Optional<Meeting> result = meetingService.getMeetingById("1");

       verify(meetingService).getMeetingById("1");

        assertTrue(result.isPresent());
        assertEquals(meeting, result.get());
    }

    @Test
    void testCreateMeeting() {
        Meeting meeting = new Meeting("1", "Meeting 1", "John", LocalDate.now(), LocalDate.now(), new HashMap<>());

        when(meetingService.createMeeting(any(Meeting.class))).thenReturn(meeting);

        Meeting result = meetingService.createMeeting(meeting);

        verify(meetingService).createMeeting(meeting);

        assertNotNull(result);
        assertEquals(meeting, result);
    }

    @Test
    void testDeleteMeeting() {
        doNothing().when(meetingService).deleteMeeting("1", "John");

        assertDoesNotThrow(() -> meetingService.deleteMeeting("1", "John"));

       verify(meetingService).deleteMeeting("1", "John");
    }

    @Test
    void testAddAttendee() {
        Meeting meeting = new Meeting("1", "Meeting 1", "John", LocalDate.now(), LocalDate.now(), new HashMap<>());

        doNothing().when(meetingService).addAttendee("1", "Alice", LocalDate.now());

        assertDoesNotThrow(() -> meetingService.addAttendee("1", "Alice", LocalDate.now()));

        verify(meetingService).addAttendee("1", "Alice", LocalDate.now());
    }

    @Test
    void testRemoveAttendee() {
        Meeting meeting = new Meeting("1", "Meeting 1", "John", LocalDate.now(), LocalDate.now(), new HashMap<>());

        doNothing().when(meetingService).removeAttendee("1", "Alice");

        assertDoesNotThrow(() -> meetingService.removeAttendee("1", "Alice"));

        verify(meetingService).removeAttendee("1", "Alice");
    }
}
