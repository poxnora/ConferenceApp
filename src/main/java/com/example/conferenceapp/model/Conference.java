package com.example.conferenceapp.model;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class Conference {
    private static final LocalDateTime start_time = LocalDateTime.of(2021, 6, 1, 10, 0);
    private static final LocalDateTime end_time = LocalDateTime.of(2021, 6, 1, 15, 45);
    private static final LocalDateTime first_lecture = LocalDateTime.of(2021, 6, 1, 10, 0);
    private static final LocalDateTime second_lecture = LocalDateTime.of(2021, 6, 1, 12, 0);
    private static final LocalDateTime third_lecture = LocalDateTime.of(2021, 6, 1, 14, 0);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy  HH:mm:ss");

    public Conference() {

    }


    public static LocalDateTime getStart_time() {
        return start_time;
    }

    public static LocalDateTime getEnd_time() {
        return end_time;
    }

    public static LocalDateTime getFirst_lecture() {
        return first_lecture;
    }


    public static LocalDateTime getSecond_lecture() {
        return second_lecture;
    }


    public static LocalDateTime getThird_lecture() {
        return third_lecture;
    }


    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String toString() {

        return "{ " +
                "\"start_time\"" + ":" + "\"" + formatter.format(start_time) + "\"" +
                ", \"end_time\"" + ":" + "\"" + formatter.format(end_time) + "\"" +
                ", \"first_lecture\"" + ":" + "\"" + formatter.format(first_lecture) + "\"" +
                ", \"second_lecture\"" + ":" + "\"" + formatter.format(second_lecture) + "\"" +
                ", \"third_lecture\"" + ":" + "\"" + formatter.format(third_lecture) + "\"" +
                "}";
    }

}
