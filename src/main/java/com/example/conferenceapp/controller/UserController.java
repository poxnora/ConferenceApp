package com.example.conferenceapp.controller;

import com.example.conferenceapp.exception.RecordNotFoundException;
import com.example.conferenceapp.model.user.User;
import com.example.conferenceapp.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() throws RecordNotFoundException {

        return new ResponseEntity<>(userService.get(), HttpStatus.OK);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserLoginsAndEmails() {
        return new ResponseEntity<>(userService.getLoginsAndEmailsList(), HttpStatus.OK);
    }


    @GetMapping(path = "/lectures", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLectureByUserLogin(@RequestParam("username") String name) {
        return (userService.getLectures(name));
    }

    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addOrUpdateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return new ResponseEntity<>(userService.updateOrSave(id, user), HttpStatus.OK);
    }

    @PutMapping(path = "/change_email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addOrUpdateUser(@RequestBody Map<String, String> data) {
        return new ResponseEntity<>(userService.updateEmail(data.get("old_email"), data.get("new_email")), HttpStatus.OK);
    }


}
