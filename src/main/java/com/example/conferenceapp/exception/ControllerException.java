package com.example.conferenceapp.exception;


public abstract class ControllerException extends RuntimeException {
    public ControllerException(String errorMessage) {
        super(errorMessage);
    }
}