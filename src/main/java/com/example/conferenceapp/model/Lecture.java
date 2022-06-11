package com.example.conferenceapp.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lectures")
public class Lecture{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1)
    private int theme;

    @Column(nullable = false, length = 1)
    private int starts;
    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "lectures")
    private Set<User> participants = new HashSet<>();

    public Lecture() {

    }

    public Lecture(long id, String title, int theme, int starts) {
        this.id = id;
        this.title = title;
        this.theme = theme;
        this.starts = starts;

    }

    public Lecture(long id, String title, int theme, int starts, Set<User> participants) {
        this.id = id;
        this.title = title;
        this.theme = theme;
        this.starts = starts;
        this.participants = participants;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getStarts() {
        return starts;
    }

    public void setStarts(int priority) {
        this.starts = priority;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }


    public String toString() {
        if (starts == 1) {
            return "{ " +
                    "\"title\"" + ":" + "\"" + title + "\"" +
                    ", \"starts\"" + ":" + "\"" + Conference.getFormatter().format(Conference.getFirst_lecture()) + "\"" +
                    "},";
        } else if (starts == 2) {
            return "{ " +
                    "\"title\"" + ":" + "\"" + title + "\"" +
                    ", \"starts\"" + ":" + "\"" + Conference.getFormatter().format(Conference.getSecond_lecture()) + "\"" +
                    "},";
        } else if (starts == 3) {
            return "{ " +
                    "\"title\"" + ":" + "\"" + title + "\"" +
                    ", \"starts\"" + ":" + "\"" + Conference.getFormatter().format(Conference.getThird_lecture()) + "\"" +
                    "},";
        }
        return "";
    }
}
