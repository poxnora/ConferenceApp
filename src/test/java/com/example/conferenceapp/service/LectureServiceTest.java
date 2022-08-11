package com.example.conferenceapp.service;

import com.example.conferenceapp.dao.LectureDao;
import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.exception.LectureServiceException;
import com.example.conferenceapp.exception.RecordNotFoundException;
import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.exception.UserServiceException;
import com.example.conferenceapp.model.Lecture;
import com.example.conferenceapp.model.user.User;
import com.example.conferenceapp.service.lecture.implementation.LectureServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @Mock
    LectureDao lectureDao;

    @Mock
    UserDao userDao;

    @InjectMocks
    LectureServiceImp lectureService;

    @Test
    void shouldReturnLecturesList() {
        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "l1", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(3L, "l3", 1, 1)
        );

        when(lectureDao.findAll()).thenReturn(lectures);
        List<Lecture> list_lectures = lectureService.get();
        assertThat(list_lectures.size()).isSameAs(lectures.size());
        verify(lectureDao).findAll();
    }


    @Test
    void shouldReturnLectureById() {
        Lecture lecture = new Lecture(1L, "l1", 1, 1);
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lecture));
        Lecture new_lecture = lectureService.getById(anyLong());
        assertThat(new_lecture).isSameAs(lecture);
        verify(lectureDao).findById(anyLong());
    }

    @Test
    void shouldNotReturnLectureById() {
        when(lectureDao.findById(anyLong())).thenThrow(RecordNotFoundException.class);
        assertThatExceptionOfType(RecordNotFoundException.class).isThrownBy(() -> lectureService.getById(anyLong()));
        verify(lectureDao).findById(anyLong());
    }

    @Test
    void shouldReturnSavedLecture() {
        Lecture lecture_new = new Lecture(1L, "l1", 1, 1);
        List<Lecture> lectures = List.of(
                new Lecture(1L, "l1", 1, 1)
        );
        when(lectureDao.findAll()).thenReturn(lectures);
        when(lectureDao.save(any(Lecture.class))).thenReturn(lecture_new);
        Lecture lecture_new1 = lectureService.save(lecture_new);
        assertThat(lecture_new).isSameAs(lecture_new1);
        verify(lectureDao).save(any(Lecture.class));
        verify(lectureDao).findAll();

    }

    @Test
    void shouldNotReturnSavedLectureInvalidTheme() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, "l1", 1, 1)
        );
        Lecture lecture_new = new Lecture(1L, "l1", -1, 1);
        when(lectureDao.findAll()).thenReturn(lectures);
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.save(lecture_new)).withMessage("Invalid theme");
    }

    @Test
    void shouldNotReturnSavedLectureMaxLectures() {

        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "l1", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(3L, "l3", 1, 1)
        );
        Lecture lecture_new = new Lecture(1L, "l1", 1, 1);
        when(lectureDao.findAll()).thenReturn(lectures);
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.save(lecture_new)).withMessage("Maximum lectures with that theme reached");
    }

    @Test
    void shouldReturnAddedOrUpdatedLecture() {
        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "l1", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(3L, "l3", 1, 1)
        );
        Lecture updated = new Lecture(1L, "l2", 2, 1);
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lectures.get(0)));
        when(lectureDao.findAll()).thenReturn(lectures);
        when(lectureDao.save(any(Lecture.class))).thenReturn(updated);
        Lecture new_lecture = lectureService.updateOrSave(1L, updated);
        assertThat(new_lecture.getTitle()).isEqualTo(updated.getTitle());
        verify(lectureDao).save(any(Lecture.class));
        verify(lectureDao).findById(anyLong());
        verify(lectureDao).findAll();
    }


    @Test
    void shouldNotReturnAddedOrUpdatedLectureInvalidTheme() {
        Lecture lecture = new Lecture(1L, "l1", 1, 1);
        Lecture updated = new Lecture(1L, "l2", 4, 1);
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.updateOrSave(1L, updated)).withMessage("Invalid theme");
    }

    @Test
    void shouldNotReturnAddedOrUpdatedLectureMaximumLectures() {
        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "l1", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(2L, "l2", 1, 1),
                new Lecture(3L, "l3", 1, 1)
        );
        Lecture updated = new Lecture(1L, "l2", 1, 1);
        when(lectureDao.findAll()).thenReturn(lectures);
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.updateOrSave(1L, updated)).withMessage("Maximum lectures with that theme reached");
        verify(lectureDao).findAll();
    }

    @Test
    void shouldReturnLectureWithAddedUser() throws IOException {
        User user = new User(5L, "user5", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        Lecture lecture = new Lecture(1L, "l1", 1, 1, new HashSet<>());
        Set<User> userSet = new HashSet<>();
        userSet.add(user);
        Lecture updated = new Lecture(1L, "l2", 2, 1, userSet);
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(lectureDao.save(lecture)).thenReturn(updated);
        Lecture new_lecture = lectureService.addUser(1L, user.getUsername(), user.getEmail());
        assertThat(new_lecture.getParticipants()).isEqualTo(userSet);
        verify(lectureDao).save(any(Lecture.class));
        verify(lectureDao).findById(anyLong());
        verify(userDao).findByUsername(anyString());
        verify(userDao).findByEmail(anyString());
    }

    @Test
    void shouldNotReturnLectureWithAddedUserAlreadyEnrolled() {
        User user = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        Set<User> userSet = new HashSet<>();
        List<Lecture> lectureSet = new ArrayList<>();
        userSet.add(user);
        Lecture lecture = new Lecture(9L, "l1", 1, 1, userSet);
        lectureSet.add(lecture);
        user.setLectures(lectureSet);
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.addUser(5L, user.getUsername(), user.getEmail())).withMessage("Cannot enroll, you've already joined lecture at this hour");

    }

    @Test
    void shouldNotReturnLectureWithAddedUserFull() {
        User user = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user1 = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user2 = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user3 = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user4 = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        Set<User> userSet = new HashSet<>();
        List<Lecture> lectureSet = new ArrayList<>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        userSet.add(user4);
        Lecture lecture = new Lecture(1L, "l1", 1, 1, userSet);
        lectureSet.add(lecture);
        user.setLectures(lectureSet);
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lecture));
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.addUser(1L, user.getUsername(), user.getEmail())).withMessage("Lecture with id: " + lecture.getId() + " is full");

    }

    @Test
    void shouldNotReturnLectureWithAddedUserInvalidCredentials() {
        User user = new User(5L, "user", "useemail.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        Set<User> userSet = new HashSet<>();
        List<Lecture> lectureSet = new ArrayList<>();
        userSet.add(user);
        Lecture lecture = new Lecture(5L, "l1", 1, 1, userSet);
        lectureSet.add(lecture);
        user.setLectures(lectureSet);
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(userDao.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.addUser(5L, user.getUsername(), user.getEmail())).withMessage("Invalid login or email");

    }

    @Test
    void shouldReturnCanceledLecture() {
        User user = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        Set<User> userSet = new HashSet<>();
        List<Lecture> lectureSet = new ArrayList<>();
        userSet.add(user);
        Lecture lecture = new Lecture(1L, "l1", 1, 1, userSet);
        lectureSet.add(lecture);
        user.setLectures(lectureSet);
        Lecture updated = new Lecture(1L, "l2", 2, 1, new HashSet<>());
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(lectureDao.save(lecture)).thenReturn(updated);
        Lecture l = lectureService.cancelUser(5L, user.getUsername(), user.getEmail());
        assertThat(l.getParticipants()).isSameAs(updated.getParticipants());
        verify(lectureDao).save(any(Lecture.class));
        verify(lectureDao).findById(anyLong());
        verify(userDao).findByUsername(anyString());
        verify(userDao).findByEmail(anyString());
    }

    @Test
    void shouldNotReturnCanceledLectureNotJoined() {
        User user = new User(5L, "user1", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        Lecture lecture = new Lecture(1L, "l1", 1, 1);
        when(lectureDao.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> lectureService.cancelUser(1L, user.getUsername(), user.getEmail())).withMessage("User with login: " + user.getUsername() + " didn't join lecture with id: " + lecture.getId());
        verify(lectureDao).findById(anyLong());
        verify(userDao).findByUsername(anyString());
        verify(userDao).findByEmail(anyString());
    }


    @Test
    void shouldNotReturnCanceledLectureInvalidCredentials() {
        User user = new User(5L, "user5", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(lectureDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThatExceptionOfType(RecordNotFoundException.class).isThrownBy(() -> lectureService.cancelUser(1L, user.getUsername(), user.getEmail()));

    }
}
