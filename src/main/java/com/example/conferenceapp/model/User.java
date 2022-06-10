package com.example.conferenceapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 30)
    private String login;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 70)
    private String password;

    @Column(nullable = false, length = 1)
    private int role;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_lectures", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name="lecture_id")})
    @JsonIgnore
    Set<Lecture> lectures = new HashSet<>();

    public User(long id, String login, String email, String password, int role,Set<Lecture> lectures) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.lectures = lectures;
    }

    public User(String login, String email, String password, int role) {

        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public User(long id, String login, String email, String password,int role) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public User() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        return "User{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
