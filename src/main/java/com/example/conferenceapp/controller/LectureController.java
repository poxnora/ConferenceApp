package com.example.conferenceapp.controller;

import com.example.conferenceapp.model.Lecture;
import com.example.conferenceapp.service.lecture.LectureService;
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

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Lecture>> getAllLectures() {
        return new ResponseEntity<>(lectureService.get(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lecture> getLectureById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(lectureService.getById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/titles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLectureTitles() {
        return new ResponseEntity<>(lectureService.getPlan(), HttpStatus.OK);
    }

    @GetMapping(path = "/popularity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLecturePopularity() {
        return new ResponseEntity<>(lectureService.lecturePopularity(), HttpStatus.OK);
    }

    @GetMapping(path = "/theme_popularity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLectureThemePopularity() {
        return new ResponseEntity<>(lectureService.themePopularity(), HttpStatus.OK);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lecture> addLecture(@RequestBody Lecture lecture) {
        return new ResponseEntity<>(lectureService.save(lecture), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lecture> addOrUpdateLecture(@PathVariable("id") Long id, @RequestBody Lecture lecture) {
        return new ResponseEntity<>(lectureService.updateOrSave(id, lecture), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lecture> addUser(@PathVariable("id") Long id, @RequestBody Map<String, String> data) throws IOException {
        return new ResponseEntity<>(lectureService.addUser(id, data.get("username"), data.get("email")), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/users/cancellation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lecture> cancelLectureUser(@PathVariable("id") Long id, @RequestBody Map<String, String> data) {
        return new ResponseEntity<>(lectureService.cancelUser(id, data.get("username"), data.get("email")), HttpStatus.OK);
    }


}
