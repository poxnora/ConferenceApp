package com.example.conferenceapp.controller;

import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.service.conference.implementation.ConferenceServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ConferenceControllerTest {


    final String details = """
                        {
                        "start_time": "01/06/2021 10:00:00",
                        "end_time": "01/06/2021 15:45:00",
                        "lectures": "01/06/2021 10:00:00,01/06/2021 12:00:00,01/06/2021 14:00:00"
            }""";
    final String all_details = """
                         {
                        "start_time": "01/06/2021 10:00:00",
                        "end_time": "01/06/2021 15:45:00",
                        "themes": 3,
                        "participants_per_lecture": 1,
                        "lectures": "01/06/2021 10:00:00,01/06/2021 12:00:00,01/06/2021 14:00:00"
            }""";

    final String conference = """
                         {
                        "start_time": "01/06/2021 10:00:00",
                        "end_time": "01/06/2021 15:45:00",
                        "themes": 3,
                        "participants": 1
                       
            }""";

    final String time = "{\"time\":\"01/06/2021 10:00:00\"}";
    @Autowired
    MockMvc mvc;
    @MockBean
    ConferenceServiceImp conferenceService;

    @Test
    public void ShouldReturnConferenceTime() throws Exception {
        when(conferenceService.get()).thenReturn(details);

        mvc.perform(get("/conference")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start_time").isNotEmpty());
        verify(conferenceService).get();


    }

    @Test
    public void ShouldReturnConferenceWithAddedTime() throws Exception {

        when(conferenceService.addLectureTime(anyString())).thenReturn(all_details);

        mvc.perform(post("/conference")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content(time))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectures").isNotEmpty());
        verify(conferenceService).addLectureTime(anyString());


    }

    @Test
    public void ShouldNotReturnConferenceWithAddedTime() throws Exception {
        when(conferenceService.addLectureTime(anyString())).thenThrow(RecordNotSavedException.class);

        mvc.perform(post("/conference")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))

                .andExpect(status().is4xxClientError());


    }


    @Test
    public void ShouldReturnEditedConference() throws Exception {

        when(conferenceService.editConference(anyString(), anyString(), anyInt(), anyInt())).thenReturn(all_details);
        mvc.perform(put("/conference")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content(conference))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start_time").value("01/06/2021 10:00:00"));
        verify(conferenceService).editConference(anyString(), anyString(), anyInt(), anyInt());


    }


    @Test
    public void ShouldNotReturnEditedConference() throws Exception {
        when(conferenceService.editConference(anyString(), anyString(), anyInt(), anyInt())).thenThrow(RecordNotSavedException.class);
        mvc.perform(put("/conference")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content(conference))
                .andExpect(status().is4xxClientError());


    }

    @Test
    public void ShouldReturnConferenceWithDeletedTime() throws Exception {
        when(conferenceService.deleteLectureTime(anyInt())).thenReturn(all_details);

        mvc.perform(delete("/conference/{number}", 1L)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectures").isNotEmpty());
        verify(conferenceService).deleteLectureTime(anyInt());


    }


    @Test
    public void ShouldNotReturnConferenceWithDeletedTime() throws Exception {
        when(conferenceService.deleteLectureTime(anyInt())).thenThrow(RecordNotSavedException.class);

        mvc.perform(delete("/conference/{number}", 1L)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))

                .andExpect(status().is4xxClientError());


    }


}