package com.example.conferenceapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecordNotSavedException extends RuntimeException {
    public RecordNotSavedException(String exception) {
        super(exception);
    }
}