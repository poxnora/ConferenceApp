package com.example.conferenceapp.model;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class Conference {
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private LocalDateTime first_lecture;
    private LocalDateTime second_lecture;
    private LocalDateTime third_lecture;


    public Conference() {

    }

    public Conference(LocalDateTime start_time, LocalDateTime end_time, LocalDateTime first_lecture, LocalDateTime second_lecture, LocalDateTime third_lecture) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.first_lecture = first_lecture;
        this.second_lecture = second_lecture;
        this.third_lecture = third_lecture;
    }


    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public LocalDateTime getFirst_lecture() {
        return first_lecture;
    }

    public void setFirst_lecture(LocalDateTime first_lecture) {
        this.first_lecture = first_lecture;
    }

    public LocalDateTime getSecond_lecture() {
        return second_lecture;
    }

    public void setSecond_lecture(LocalDateTime second_lecture) {
        this.second_lecture = second_lecture;
    }

    public LocalDateTime getThird_lecture() {
        return third_lecture;
    }

    public void setThird_lecture(LocalDateTime third_lecture) {
        this.third_lecture = third_lecture;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy  HH:mm:ss");
        return "{ " +
                "\"start_time\"" + ":" + "\"" + formatter.format(start_time) + "\"" +
                ", \"end_time\"" + ":" + "\"" + formatter.format(end_time) + "\"" +
                ", \"first_lecture\"" + ":" + "\"" + formatter.format(first_lecture) + "\"" +
                ", \"second_lecture\"" + ":" + "\"" + formatter.format(second_lecture) + "\"" +
                ", \"third_lecture\"" + ":" + "\"" + formatter.format(third_lecture) + "\"" +
                "}";
    }

}
