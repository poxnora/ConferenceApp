package com.example.conferenceapp.service.lecture;

import com.example.conferenceapp.model.Lecture;

import java.io.IOException;
import java.util.List;

public interface LectureService {
    List<Lecture> get();

    String getPlan();

    Lecture getById(Long id);

    Lecture save(Lecture lecture);

    Lecture updateOrSave(Long id, Lecture lecture);

    Lecture addUser(Long id, String login, String email) throws IOException;

    Lecture cancelUser(Long id, String login, String email);

    String lecturePopularity();

    String themePopularity();
}
