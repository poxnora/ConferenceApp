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
        return new Conference().toString();
    }
}
