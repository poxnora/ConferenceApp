package com.example.conferenceapp.exceptions;


public class LectureServiceException extends Exception {

    public LectureServiceException() {
        super();
    }

    public LectureServiceException(final String message) {
        super(message);
    }
}