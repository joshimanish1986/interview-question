package com.example.demo.exception;

public class CourseIsFullException extends Exception {
    public CourseIsFullException(String msg) {
        super(msg);
    }
}
