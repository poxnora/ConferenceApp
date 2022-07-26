package com.example.conferenceapp.controller;


import com.example.conferenceapp.exception.RecordNotFoundException;
import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.model.user.User;
import com.example.conferenceapp.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {


    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;


    @Test
    void shouldReturnUsersList() throws Exception {

        List<User> users = Arrays.asList(
                new User(1L, "user1", "user1@email.com", "pass1", "USER"),
                new User(2L, "user2", "user2@email.com", "pass1", "USER"),
                new User(3L, "user3", "user3@email.com", "pass1", "USER")
        );

        when(userService.get()).thenReturn(users);

        mvc.perform(get("/users/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[*].id").isNotEmpty());
        verify(userService).get();
    }

    @Test
    void shouldReturnUsersById() throws Exception {

        User user = new User(1L, "user1", "user1@email.com", "pass1", "USER");
        when(userService.getById(anyLong())).thenReturn(user);

        mvc.perform(get("/users/details/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
        verify(userService).getById(user.getId());
    }

    @Test
    void shouldNotReturnUsersById() throws Exception {
        when(userService.getById(12312L)).thenThrow(RecordNotFoundException.class);

        mvc.perform(get("/users/details/{id}", 12312)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService).getById(12312L);
    }


    @Test
    void shouldReturnUserLectures() throws Exception {

        String lecture = "[{\"title\":\"l1\"},{\"title\":\"l2\"}]";

        when(userService.getLectures(anyString())).thenReturn(lecture);
        mvc.perform(get("/users/lectures")
                        .param("username", anyString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("l1"));
        verify(userService).getLectures(anyString());
    }

    @Test
    void shouldNotReturnUserLectures() throws Exception {


        when(userService.getLectures(anyString())).thenThrow(RecordNotFoundException.class);
        mvc.perform(get("/users/lectures")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnAddedUser() throws Exception {

        User user = new User(5L, "user5", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userService.save(any(User.class))).thenReturn(user);
        mvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":5,\"username\":\"user5\",\"email\":\"user5@email.com\",\"password\":\"$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m\",\"authority\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.id").value(user.getId()));
        verify(userService).save(any(User.class));
    }

    @Test
    void shouldNotReturnAddedUser() throws Exception {
        when(userService.save(any(User.class))).thenThrow(RecordNotSavedException.class);
        mvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"id\":5,\"username\":\"user5\",\"email\":\"useremail.com\",\"password\":\"$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m\",\"authority\":\"USER\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnAddedUserById() throws Exception {
        User user = new User(5L, "user5", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");

        when(userService.updateOrSave(anyLong(), any(User.class))).thenReturn(user);
        mvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":5,\"username\":\"user5\",\"email\":\"useremail.com\",\"password\":\"$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m\",\"authority\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.id").value(user.getId()));
        verify(userService).updateOrSave(anyLong(), any(User.class));
    }


    @Test
    void shouldNotReturnAddedUserById() throws Exception {
        when(userService.updateOrSave(anyLong(), any(User.class))).thenThrow(RecordNotSavedException.class);
        mvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"id\":5,\"username\":\"user5\",\"email\":\"useremail.com\",\"password\":\"$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m\",\"authority\":\"USER\"}"))
                .andExpect(status().is4xxClientError());
        verify(userService).updateOrSave(anyLong(), any(User.class));
    }

    @Test
    void shouldReturnUserWithChangedEmail() throws Exception {
        User user = new User(5L, "user5", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userService.updateEmail(anyString(), anyString())).thenReturn(user);
        mvc.perform(put("/users/change_email")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"old_email\":\"email@email.com\",\"new_email\":\"email1@email.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
        verify(userService).updateEmail(anyString(), anyString());
    }

    @Test
    void shouldNotReturnUserWithChangedEmail() throws Exception {
        when(userService.updateEmail(anyString(), anyString())).thenThrow(RecordNotSavedException.class);
        mvc.perform(put("/users/change_email")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"old_email\":\"email@email.com\",\"new_email\":\"email1@email.com\"}"))
                .andExpect(status().is4xxClientError());
        verify(userService).updateEmail(anyString(), anyString());
    }

}
