package com.example.conferenceapp.controller;

import com.example.conferenceapp.service.conference.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;


    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPlan() {
        return new ResponseEntity<>(conferenceService.get(), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addLectureTime(@RequestBody Map<String, String> data) {
        return new ResponseEntity<>(conferenceService.addLectureTime(data.get("time")), HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editConference(@RequestBody Map<String, String> data) {
        return new ResponseEntity<>(conferenceService.editConference(data.get("start_time"), data.get("end_time"), Integer.valueOf(data.get("themes")), Integer.parseInt(data.get("participants"))), HttpStatus.OK);
    }


    @DeleteMapping(path = "/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteLectureTime(@PathVariable("number") int number) {
        return new ResponseEntity<>(conferenceService.deleteLectureTime(number), HttpStatus.OK);
    }

}
