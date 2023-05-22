package com.example.demo;


import com.example.demo.controller.MeetingController;
import com.example.demo.entity.Meeting;
import com.example.demo.service.MeetingService;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.client.match.ContentRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDate;
import java.util.*;


import static java.lang.reflect.Array.get;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MeetingControllerTest {

    @Mock
    private MeetingService meetingService;

    @InjectMocks
    private MeetingController meetingController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(meetingController).build();
    }

    @Test
    public void testGetAllMeetings() throws Exception {
        List<Meeting> meetings = Arrays.asList(
                new Meeting("1", "Meeting 1", "John Doe", LocalDate.now(), LocalDate.now(), new HashMap<>()),
                new Meeting("2", "Meeting 2", "Jane Smith", LocalDate.now(), LocalDate.now(), new HashMap<>())
        );

        when(meetingService.getAllMeetings(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt()))
                .thenReturn(meetings);

        mockMvc.perform(get("/meetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].description", is("Meeting 1")))
                .andExpect(jsonPath("$[0].responsiblePerson", is("John Doe")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].description", is("Meeting 2")))
                .andExpect(jsonPath("$[1].responsiblePerson", is("Jane Smith")));

        verify(meetingService).getAllMeetings(null, null, null, null, null, null, null);
    }

    @Test
    public void testGetMeetingById() throws Exception {
        Meeting meeting = new Meeting("1", "Meeting 1", "John Doe", LocalDate.now(), LocalDate.now(), new HashMap<>());

        when(meetingService.getMeetingById("1")).thenReturn(Optional.of(meeting));

        mockMvc.perform((RequestBuilder) get("/meetings/{id}", 1))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is("1")))
                .andExpect((ResultMatcher) jsonPath("$.description", is("Meeting 1")))
                .andExpect((ResultMatcher) jsonPath("$.responsiblePerson", is("John Doe")));

        verify(meetingService).getMeetingById("1");
    }

    @Test
    public void testCreateMeeting() throws Exception {
        Meeting meeting = new Meeting("1", "Meeting 1", "John Doe", LocalDate.now(), LocalDate.now(), new HashMap<>());

        when(meetingService.createMeeting(any(Meeting.class))).thenReturn(meeting);

        mockMvc.perform(post("/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meeting)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.description", is("Meeting 1")))
                .andExpect(jsonPath("$.responsiblePerson", is("John Doe")));

        verify(meetingService).createMeeting(any(Meeting.class));
    }

    private ContentRequestMatchers post(String s) {
    }

    @Test
    public void testDeleteMeeting() throws Exception {
        mockMvc.perform(delete("/meetings/{id}?requester={requester}", "1", "John Doe"))
                .andExpect(status().isNoContent());

        verify(meetingService).deleteMeeting("1", "John Doe");
    }

    @Test
    public void testAddAttendee() throws Exception {
        mockMvc.perform(post("/meetings/{id}/attendees?attendee={attendee}&time={time}", "1", "John Smith", LocalDate.now())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(meetingService).addAttendee("1", "John Smith", LocalDate.now());
    }

    @Test
    public void testRemoveAttendee() throws Exception {
        mockMvc.perform(delete("/meetings/{id}/attendees/{attendee}", "1", "John Smith"))
                .andExpect(status().isOk());

        verify(meetingService).removeAttendee("1", "John Smith");
    }

    // Add more test methods for other controller endpoints

}
