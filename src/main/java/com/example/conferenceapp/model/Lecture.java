package com.example.conferenceapp.model;

import com.example.conferenceapp.model.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1)
    private Integer theme;

    @Column(nullable = false, length = 1)
    private Integer starts;
    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "lectures")
    private Set<User> participants = new HashSet<>();

    public Lecture() {

    }

    public Lecture(Long id, String title, Integer theme, Integer starts) {
        this.id = id;
        this.title = title;
        this.theme = theme;
        this.starts = starts;

    }

    public Lecture(Long id, String title, Integer theme, Integer starts, Set<User> participants) {
        this.id = id;
        this.title = title;
        this.theme = theme;
        this.starts = starts;
        this.participants = participants;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public Integer getStarts() {
        return starts;
    }

    public void setStarts(Integer priority) {
        this.starts = priority;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }


    public String toString() {
        try {
            return "{ " +
                    "\"title\"" + ":" + "\"" + title + "\"" +
                    ", \"starts\"" + ":" + "\"" + Conference.getFormatter().format(Conference.getLectures_times().get(starts - 1)) + "\"" +
                    "}";


        } catch (Exception e) {
            return "{ " +
                    "\"title\"" + ":" + "\"" + title + "\"" +
                    ", \"starts\"" + ":" + "\"Not added\"" +
                    "}";
        }
    }
}

