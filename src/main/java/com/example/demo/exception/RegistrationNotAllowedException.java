package com.example.demo.exception;

public class RegistrationNotAllowedException extends Exception {
    public RegistrationNotAllowedException(String msg) {
        super(msg);
    }
}
