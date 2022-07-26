package com.example.conferenceapp.model;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class Conference {


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    static LocalDateTime start_time = LocalDateTime.of(2021, 6, 1, 10, 0);
    static Integer themes = 3;
    static Integer participants_per_lecture = 5;
    static LocalDateTime end_time = LocalDateTime.of(2021, 6, 1, 15, 45);
    static List<LocalDateTime> lectures_times = List.of(LocalDateTime.of(2021, 6, 1, 10, 0), LocalDateTime.of(2021, 6, 1, 12, 0), LocalDateTime.of(2021, 6, 1, 14, 0));

    public Conference() {


    }


    public static LocalDateTime getStart_time() {
        return start_time;
    }

    public static void setStart_time(LocalDateTime start_time) {
        Conference.start_time = start_time;
    }

    public static LocalDateTime getEnd_time() {
        return end_time;
    }

    public static void setEnd_time(LocalDateTime end_time) {
        Conference.end_time = end_time;
    }

    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

    public static Integer getParticipants_per_lecture() {
        return participants_per_lecture;
    }

    public static void setParticipants_per_lecture(Integer participants_per_lecture) {
        Conference.participants_per_lecture = participants_per_lecture;
    }

    public static List<LocalDateTime> getLectures_times() {
        return lectures_times;
    }

    public static void setLectures_times(List<LocalDateTime> lectures_times) {
        Conference.lectures_times = lectures_times;
    }

    public static Integer getThemes() {
        return themes;
    }

    public static void setThemes(Integer themes) {
        Conference.themes = themes;
    }

    static public String allDetails() {
        StringBuilder lectures = new StringBuilder();
        for (LocalDateTime l : lectures_times) {
            lectures.append(formatter.format(l));
            lectures.append(",");
        }
        if (lectures.length() > 0)
            lectures.setLength(lectures.length() - 1);

        return "{ " +
                "\"start_time\"" + ":" + "\"" + formatter.format(start_time) + "\"" +
                ", \"end_time\"" + ":" + "\"" + formatter.format(end_time) + "\"" +
                ", \"themes\"" + ":" + themes +
                ", \"participants_per_lecture\"" + ":" + participants_per_lecture +
                ", \"lectures\"" + ":" + "\"" + lectures + "\"" +
                "}";
    }


    public static String details() {

        StringBuilder lectures = new StringBuilder();
        for (LocalDateTime l : lectures_times) {
            lectures.append(formatter.format(l));
            lectures.append(",");
        }
        if (lectures.length() > 0)
            lectures.setLength(lectures.length() - 1);

        return "{ " +
                "\"start_time\"" + ":" + "\"" + formatter.format(start_time) + "\"" +
                ", \"end_time\"" + ":" + "\"" + formatter.format(end_time) + "\"" +
                ", \"lectures\"" + ":" + "\"" + lectures + "\"" +
                "}";
    }

}
