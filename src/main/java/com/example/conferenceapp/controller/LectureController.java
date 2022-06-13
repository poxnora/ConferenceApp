package com.example.conferenceapp.controller;

import com.example.conferenceapp.exceptions.LectureServiceException;
import com.example.conferenceapp.exceptions.UserServiceException;
import com.example.conferenceapp.model.Lecture;
import com.example.conferenceapp.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/lectures")
public class LectureController {

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    private final LectureService lectureService;

    @GetMapping("/show")
    public List<Lecture> getAllLectures() {
        return lectureService.get();
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Lecture> getLectureById(@PathVariable("id") long id) {
        return new ResponseEntity<>(lectureService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLectureTitles() {
        return lectureService.get_plan();
    }

    @GetMapping(value = "/popularity", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLecturePopularity() {
        return lectureService.lecturePopularity();
    }

    @GetMapping(value = "/theme_popularity", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLectureThemePopularity() {
        return lectureService.themePopularity();
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLectureByUserLogin(@RequestBody Map<String, String> data) throws UserServiceException {
        return (lectureService.getLectures(data.get("username")));
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Lecture> addLecture(@RequestBody Lecture lecture) throws LectureServiceException {
        return new ResponseEntity<>(lectureService.save(lecture), HttpStatus.CREATED);
    }

    @PutMapping(path = "/add/{id}")
    public ResponseEntity<Lecture> addOrUpdateLecture(@PathVariable("id") long id, @RequestBody Lecture lecture) throws LectureServiceException {
        return new ResponseEntity<>(lectureService.updateOrSave(id, lecture), HttpStatus.OK);
    }

    @PutMapping(path = "{id}/add_user")
    public ResponseEntity<Lecture> addUser(@PathVariable("id") long id, @RequestBody Map<String, String> data) throws UserServiceException, LectureServiceException, IOException {
        return new ResponseEntity<>(lectureService.addUser(id, data.get("username"), data.get("email")), HttpStatus.OK);
    }

    @PutMapping(path = "{id}/cancel_user")
    public ResponseEntity<Lecture> cancelLectureUser(@PathVariable("id") long id, @RequestBody Map<String, String> data) throws UserServiceException, LectureServiceException {
        return new ResponseEntity<>(lectureService.cancelUser(id, data.get("username"), data.get("email")), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteLecture(@PathVariable("id") long id) {
        lectureService.delete(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }

}
