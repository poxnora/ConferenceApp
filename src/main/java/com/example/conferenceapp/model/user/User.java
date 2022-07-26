package com.example.conferenceapp.model.user;

import com.example.conferenceapp.model.Lecture;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_lectures", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "lecture_id")})
    @JsonIgnore
    List<Lecture> lectures = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String username;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 70)
    private String password;
    @Column(nullable = false, length = 5)
    private String authority;

    public User(Long id, String username, String email, String password, String authority, List<Lecture> lectures) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authority = authority;
        this.lectures = lectures;
    }

    public User(String username, String email, String password, String authority) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.authority = authority;

    }

    public User(Long id, String username, String email, String password, String authority) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authority = authority;

    }

    public User() {

    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String role) {
        this.authority = role;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{ " +
                "\"username\"" + ":" + "\"" + username + "\"" +
                ", \"email\"" + ":" + "\"" + email + "\"" +
                "}";
    }
}
