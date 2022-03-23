package com.example.demo.exception;

public class CancellationNotAllowedException extends Exception {
    public CancellationNotAllowedException(String msg) {
        super(msg);
    }
}
