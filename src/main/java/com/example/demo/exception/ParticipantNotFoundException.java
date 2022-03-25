package com.example.demo.exception;

public class ParticipantNotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParticipantNotFoundException(String msg) {
        super(msg);
    }
}
