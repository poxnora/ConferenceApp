package com.example.conferenceapp.service.user;

import com.example.conferenceapp.model.user.User;

import java.util.List;

public interface UserService {
    List<User> get();

    String getLoginsAndEmailsList();

    String getLoginAndEmail(Long id);

    User getById(Long id);

    User save(User user);

    User updateOrSave(Long id, User user);

    User updateEmail(String old_email, String new_email);

    String getLectures(String login);

}
