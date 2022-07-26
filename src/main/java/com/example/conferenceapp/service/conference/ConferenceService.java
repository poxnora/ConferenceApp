package com.example.conferenceapp.service.conference;

public interface ConferenceService {

    String get();

    String editConference(String start, String end, Integer theme, Integer participants);

    String addLectureTime(String time);

    String deleteLectureTime(int number);
}
