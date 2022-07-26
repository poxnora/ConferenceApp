package com.example.conferenceapp.controller;


import com.example.conferenceapp.exception.RecordNotFoundException;
import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.model.Conference;
import com.example.conferenceapp.model.Lecture;
import com.example.conferenceapp.model.user.User;
import com.example.conferenceapp.service.lecture.implementation.LectureServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest

@AutoConfigureMockMvc(addFilters = false)
public class LectureControllerTest {


    @Autowired
    MockMvc mvc;

    @MockBean
    LectureServiceImp lectureService;


    @Test
    void shouldReturnLecturesList() throws Exception {

        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "l1", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(3L, "l3", 1, 1)
        );

        when(lectureService.get()).thenReturn(lectures);

        mvc.perform(get("/lectures")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[*].id").isNotEmpty());
        verify(lectureService).get();
    }

    @Test
    void shouldReturnLecturesById() throws Exception {

        Lecture lecture = new Lecture(1L, "l1", 1, 1);
        when(lectureService.getById(anyLong())).thenReturn(lecture);

        mvc.perform(get("/lectures/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(lecture.getId()))
                .andExpect(jsonPath("$.title").value(lecture.getTitle()));
        verify(lectureService).getById(lecture.getId());
    }

    @Test
    void shouldNotReturnLecturesById() throws Exception {
        when(lectureService.getById(anyLong())).thenThrow(RecordNotFoundException.class);

        mvc.perform(get("/lectures/{id}", anyLong())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(lectureService).getById(anyLong());
    }

    @Test
    void shouldReturnLecturesTitles() throws Exception {

        String lecture = "{ " +
                "\"title\"" + ":" + "\"" + "l1" + "\"" +
                ", \"starts\"" + ":" + "\"" + Conference.getFormatter().format(Conference.getLectures_times().get(0)) + "\"" +
                "}";

        when(lectureService.getPlan()).thenReturn(lecture);
        mvc.perform(get("/lectures/titles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("l1"));
        verify(lectureService).getPlan();
    }

    @Test
    void shouldReturnLecturesPopularity() throws Exception {

        String lecture = "{\"Lecture\":1,\"Popularity\":12}";

        when(lectureService.lecturePopularity()).thenReturn(lecture);
        mvc.perform(get("/lectures/popularity")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Popularity").value(12));
        verify(lectureService).lecturePopularity();
    }

    @Test
    void shouldReturnLecturesThemePopularity() throws Exception {

        String lecture = "{\"Theme\":1,\"Popularity\":32}";
        when(lectureService.themePopularity()).thenReturn(lecture);
        mvc.perform(get("/lectures/theme_popularity")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Popularity").value(32));
        verify(lectureService).themePopularity();
    }

    @Test
    void shouldNotReturnAddedLecture() throws Exception {

        when(lectureService.save(any(Lecture.class))).thenThrow(RecordNotSavedException.class);
        mvc.perform(post("/lectures")
                        .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":1,\"title\":\"l1\",\"theme\":1,\"starts\":1}"))
                .andExpect(status().is4xxClientError());
        verify(lectureService).save(any(Lecture.class));
    }

    @Test
    void shouldReturnAddedLecture() throws Exception {
        Lecture lecture = new Lecture(1L, "l2", 2, 2);
        when(lectureService.save(any(Lecture.class))).thenReturn(lecture);
        mvc.perform(post("/lectures")
                        .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":1,\"title\":\"l2\",\"theme\":2,\"starts\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(lecture.getTitle()))
                .andExpect(jsonPath("$.theme").value(lecture.getTheme()));
        verify(lectureService).save(any(Lecture.class));
    }


    @Test
    void shouldReturnAddedLectureById() throws Exception {
        Lecture updated = new Lecture(1L, "l2", 2, 2);
        when(lectureService.updateOrSave(anyLong(), any(Lecture.class))).thenReturn(updated);
        mvc.perform(put("/lectures/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":1,\"title\":\"l2\",\"theme\":2,\"starts\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updated.getTitle()))
                .andExpect(jsonPath("$.theme").value(updated.getTheme()));
        verify(lectureService).updateOrSave(anyLong(), any(Lecture.class));
    }

    @Test
    void shouldNotReturnAddedLectureById() throws Exception {
        when(lectureService.updateOrSave(anyLong(), any(Lecture.class))).thenThrow(RecordNotSavedException.class);
        mvc.perform(put("/lectures/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":1,\"title\":\"l2\",\"theme\":32,\"starts\":2}"))
                .andExpect(status().is4xxClientError());
        verify(lectureService).updateOrSave(anyLong(), any(Lecture.class));
    }

    @Test
    void shouldReturnLectureWithAddedUser() throws Exception {
        Set<User> set = new HashSet<>();
        User user = new User(5L, "user5", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        set.add(user);
        Lecture updated = new Lecture(1L, "l2", 2, 2, set);
        when(lectureService.addUser(anyLong(), anyString(), anyString())).thenReturn(updated);
        mvc.perform(put("/lectures/{id}/users", 7)
                        .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":5,\"username\":\"user5\",\"email\":\"user5@email.com\",\"password\":\"$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m\",\"authority\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants[0].id").value(5))
                .andExpect(jsonPath("$.participants[0].username").value("user5"));
        verify(lectureService).addUser(anyLong(), anyString(), anyString());

    }

    @Test
    void shouldNotReturnLectureWithAddedUser() throws Exception {
        when(lectureService.addUser(anyLong(), anyString(), anyString())).thenThrow(RecordNotSavedException.class);
        mvc.perform(put("/lectures/{id}/users", 1)
                        .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":5,\"username\":\"user5\",\"email\":\"user5@email.com\",\"password\":\"$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m\",\"authority\":\"USER\"}"))
                .andExpect(status().is4xxClientError());
        verify(lectureService).addUser(anyLong(), anyString(), anyString());

    }
}
