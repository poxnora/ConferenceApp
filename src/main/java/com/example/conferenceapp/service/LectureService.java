package com.example.conferenceapp.service;

import com.example.conferenceapp.exceptions.LectureServiceException;
import com.example.conferenceapp.exceptions.UserServiceException;
import com.example.conferenceapp.model.Lecture;

import java.io.IOException;
import java.util.List;

public interface LectureService {
    List<Lecture> get();

    String get_plan();

    Lecture getById(long id);

    Lecture save(Lecture lecture) throws LectureServiceException;

    void delete(long id);

    Lecture updateOrSave(long id, Lecture lecture) throws LectureServiceException;

    Lecture addUser(long id, String login, String email) throws LectureServiceException, UserServiceException, IOException;


    String getLectures(String login) throws UserServiceException;

    Lecture cancelUser(long id, String login, String email) throws LectureServiceException, UserServiceException;

    String lecturePopularity();

    String themePopularity();
}
