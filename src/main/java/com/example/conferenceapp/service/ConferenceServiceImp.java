package com.example.conferenceapp.service;

import com.example.conferenceapp.dao.LectureDao;
import com.example.conferenceapp.model.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConferenceServiceImp implements ConferenceService {

    @Autowired
    LectureDao lectureDao;

    @Override
    public String get() {
        Conference conference = new Conference(LocalDateTime.of(2021, 6, 1, 10, 45), LocalDateTime.of(2021, 6, 1, 15, 45), LocalDateTime.of(2021, 6, 1, 10, 0), LocalDateTime.of(2021, 6, 1, 12, 0), LocalDateTime.of(2021, 6, 1, 13, 45));
        return conference.toString();
    }
}
