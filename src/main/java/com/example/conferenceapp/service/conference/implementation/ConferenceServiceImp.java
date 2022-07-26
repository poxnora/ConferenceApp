package com.example.conferenceapp.service.conference.implementation;

import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.model.Conference;
import com.example.conferenceapp.service.conference.ConferenceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConferenceServiceImp implements ConferenceService {


    @Override
    public String get() {
        return Conference.details();
    }

    @Override
    public String editConference(String start, String end, Integer themes, Integer participants) {
        try {
            LocalDateTime start_time = LocalDateTime.parse(start, Conference.getFormatter());
            LocalDateTime end_time = LocalDateTime.parse(end, Conference.getFormatter());
            if (end_time.isBefore(start_time))
                throw new RecordNotSavedException("Invalid date");
            if (end_time.isEqual(start_time))
                throw new RecordNotSavedException("Invalid date");
            Conference.setStart_time(start_time);
            Conference.setEnd_time(end_time);
            if (themes <= 0) {
                throw new RecordNotSavedException("Theme must be larger than 0");
            }
            if (participants <= 0 || participants > 1000) {
                throw new RecordNotSavedException("Invalid participants number");
            }
            Conference.setThemes(themes);
            Conference.setParticipants_per_lecture(participants);
            return Conference.allDetails();
        } catch (Exception e) {
            throw new RecordNotSavedException(e.getLocalizedMessage());
        }


    }

    @Override
    public String addLectureTime(String time) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(time, Conference.getFormatter());
            if (localDateTime.isBefore(LocalDateTime.now()))
                throw new RecordNotSavedException("Date cannot be before now");
            List<LocalDateTime> localDateTimes = new ArrayList<>(Conference.getLectures_times());
            localDateTimes.add(localDateTime);
            Conference.setLectures_times(localDateTimes);
            return Conference.allDetails();
        } catch (Exception e) {
            throw new RecordNotSavedException(e.getLocalizedMessage());
        }


    }

    @Override
    public String deleteLectureTime(int number) {
        try {
            List<LocalDateTime> localDateTimes = new ArrayList<>(Conference.getLectures_times());
            localDateTimes.remove(number);
            Conference.setLectures_times(localDateTimes);
            return Conference.allDetails();
        } catch (Exception e) {
            throw new RecordNotSavedException("Invalid lecture index");
        }

    }
}
