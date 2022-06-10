package com.example.conferenceapp.service;

import com.example.conferenceapp.exceptions.UserServiceException;
import com.example.conferenceapp.model.User;

import java.util.List;

public interface UserService {
    List<User> get();

    String get_LoginsAndEmails();

    User getById(long id);

    User save(User user) throws UserServiceException;

    void delete(long id);

    User updateOrSave(long id, User user) throws UserServiceException;

    User updateEmail(String old_email, String new_email) throws UserServiceException;


}
