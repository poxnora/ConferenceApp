package com.example.conferenceapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 30)
    private String username;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 70)
    private String password;

    @Column(nullable = false, length = 5)
    private String authority;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_lectures", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "lecture_id")})
    @JsonIgnore
    Set<Lecture> lectures = new HashSet<>();

    public User(long id, String username, String email, String password, String authority, Set<Lecture> lectures) {
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

    public User(long id, String username, String email, String password, String authority) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authority = authority;

    }

    public User() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String role) {
        this.authority = role;
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }

    public long getId() {
        return id;
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

    @Override
    public String toString() {
        return "{ " +
                "\"username\"" + ":" + "\"" + username + "\"" +
                ", \"email\"" + ":" + "\"" + email + "\"" +
                "},";
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
