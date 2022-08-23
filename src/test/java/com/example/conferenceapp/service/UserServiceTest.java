package com.example.conferenceapp.service;

import com.example.conferenceapp.dao.LectureDao;
import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.exception.RecordNotFoundException;
import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.model.user.User;
import com.example.conferenceapp.service.user.implementation.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    LectureDao lectureDao;

    @Mock
    UserDao userDao;

    @InjectMocks
    UserServiceImp userService;

    @Test
    void shouldReturnUsers() {
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );

        when(userDao.findAll()).thenReturn(users);
        List<User> userList = userService.get();
        assertThat(userList.size()).isSameAs(users.size());
        verify(userDao).findAll();
    }

    @Test
    void shouldReturnUsersById() {
        User user = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        User new_user = userService.getById(anyLong());
        assertThat(user).isSameAs(new_user);
        verify(userDao).findById(anyLong());
    }

    @Test
    void shouldNotReturnUsersById() {
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThatExceptionOfType(RecordNotFoundException.class).isThrownBy(() -> userService.getById(anyLong()));
        verify(userDao).findById(anyLong());
    }

    @Test
    void shouldReturnAddedUser() {
        User user = new User("user3", "user3@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        when(userDao.save(any(User.class))).thenReturn(user);
        when(userDao.findAll()).thenReturn(users);
        User new_user = userService.save(user);
        assertThat(new_user).isSameAs(user);
        verify(userDao).save(any(User.class));
        verify(userDao).findAll();

    }

    @Test
    void shouldNotReturnAddedUserLoginAlreadyExits() {
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        when(userDao.findAll()).thenReturn(users);
        User user = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.save(user)).withMessage("User already exists");
        verify(userDao).findAll();

    }

    @Test
    void shouldNotReturnAddedUserInvalidEmail() {
        User user = new User("user", "useremail.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.save(user)).withMessage("Invalid email");

    }


    @Test
    void shouldNotReturnAddedUserAlreadyExists() {
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        when(userDao.findAll()).thenReturn(users);
        User user = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.save(user)).withMessage("User already exists");
        verify(userDao).findAll();

    }

    @Test
    void shouldReturnAddedOrUpdatedUser() {
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        User user_updated = new User("user3", "user3@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userDao.findById(anyLong())).thenReturn(Optional.ofNullable(users.get(0)));
        when(userDao.findAll()).thenReturn(users);
        when(userDao.save(users.get(0))).thenReturn(user_updated);
        User user_new = userService.updateOrSave(1L, user_updated);
        assertThat(user_new.getUsername()).isEqualTo(user_updated.getUsername());
        verify(userDao).save(any(User.class));
        verify(userDao).findById(anyLong());
        verify(userDao).findAll();

    }

    @Test
    void shouldNotReturnAddedOrUpdatedUserInvalidEmail() {
        User user = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user_updated = new User("user1", "useremail.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.updateOrSave(1L, user_updated)).withMessage("Invalid email");
        verify(userDao).findById(anyLong());
    }

    @Test
    void shouldNotReturnAddedOrUpdatedUserAlreadyExist() {
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        User user = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user_updated = new User("user2", "user2@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        when(userDao.findAll()).thenReturn(users);
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.updateOrSave(1L, user_updated)).withMessage("User already exists");
        verify(userDao).findById(anyLong());
        verify(userDao).findAll();


    }


    @Test
    void shouldNotReturnAddedUserEmailAlreadyExits() {
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        when(userDao.findAll()).thenReturn(users);
        User user = new User("user123", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.save(user)).withMessage("User already exists");
        verify(userDao).findAll();


    }

    @Test
    void shouldReturnUserWithUpdatedEmail() {
        User user = new User(5L, "user5", "user5@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user_updated = new User(5L, "user5", "usernew@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userDao.save(user)).thenReturn(user_updated);
        User new_user = userService.updateEmail(user.getEmail(), user_updated.getEmail());
        assertThat(new_user).isEqualTo(user_updated);
        verify(userDao).save(any(User.class));
    }

    @Test
    void shouldNotReturnUserWithUpdatedEmailInvalidEmail() {
        User user = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user_updated = new User("user", "useremail.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.updateEmail(user.getEmail(), user_updated.getEmail())).withMessage("Invalid email");


    }

    @Test
    void shouldNotReturnUserWithUpdatedEmailSameEmail() {
        User user = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        User user_updated = new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.updateEmail(user.getEmail(), user_updated.getEmail())).withMessage("Emails must be different");
    }

    @Test
    void shouldNotReturnUserWithUpdatedEmailAlreadyExists() {
        List<User> users = Arrays.asList(
                new User("user", "user@email.com", "$2a$10$pErf5KdHABEg0Knasdas1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        User user_updated = new User("user", "user1@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userDao.findAll()).thenReturn(users);
        assertThatExceptionOfType(RecordNotSavedException.class).isThrownBy(() -> userService.updateEmail(users.get(0).getEmail(), user_updated.getEmail())).withMessage("Email exists");
        verify(userDao).findAll();

    }


    @Test
    void shouldNotReturnUserWithUpdatedEmailNoUser() {
        List<User> users = Arrays.asList(
                new User("user1", "user1@email.com", "$2a$10$pErf5KddasBEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER"),
                new User("user2", "user2@email.com", "$2a$10$pErf5KdHAasdasEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER")
        );
        User user_updated = new User("user", "user3@email.com", "$2a$10$pErf5KdHABEg0KnIWDaR1ey8lB4b6lcS0V/mODGVZzSlwXXSIA3/m", "USER");
        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userDao.findAll()).thenReturn(users);
        assertThatExceptionOfType(RecordNotFoundException.class).isThrownBy(() -> userService.updateEmail("user@email.com", user_updated.getEmail())).withMessage("No user with email: " + "user@email.com");
        verify(userDao).findByEmail(anyString());
        verify(userDao).findAll();

    }

}
