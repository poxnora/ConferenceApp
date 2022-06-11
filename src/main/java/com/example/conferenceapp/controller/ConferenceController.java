package com.example.conferenceapp.controller;

import com.example.conferenceapp.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/conference")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDetails() {
        return conferenceService.get();
    }
}
