package com.example.conferenceapp.controller;

import com.example.conferenceapp.exceptions.RecordNotFoundException;
import com.example.conferenceapp.exceptions.UserServiceException;
import com.example.conferenceapp.model.User;
import com.example.conferenceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() throws RecordNotFoundException {

        List<User> users = userService.get();
        if (users == null) {
            throw new RecordNotFoundException("No users");
        }
        return users;


    }

    @GetMapping(value = "/show", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllUsersLoginAndEmails() throws RecordNotFoundException {

        return userService.get_LoginsAndEmails();


    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<User> addUser(@RequestBody User user) throws UserServiceException {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PutMapping(path = "/add/{id}")
    public ResponseEntity<User> addOrUpdateUser(@PathVariable("id") long id, @RequestBody User user) throws UserServiceException {
        return new ResponseEntity<>(userService.updateOrSave(id, user), HttpStatus.OK);
    }

    @PutMapping(path = "/email")
    public ResponseEntity<User> addOrUpdateUser(@RequestBody Map<String, String> data) throws UserServiceException {
        return new ResponseEntity<>(userService.updateEmail(data.get("old_email"), data.get("new_email")), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
