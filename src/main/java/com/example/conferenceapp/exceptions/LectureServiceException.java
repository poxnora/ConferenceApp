package com.example.conferenceapp.exceptions;


public class LectureServiceException extends Exception {

    private static final long serialVersionUID = -470180507998010368L;

    public LectureServiceException() {
        super();
    }

    public LectureServiceException(final String message) {
        super(message);
    }
}