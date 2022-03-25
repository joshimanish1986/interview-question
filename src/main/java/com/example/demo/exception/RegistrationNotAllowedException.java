package com.example.demo.exception;

public class RegistrationNotAllowedException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegistrationNotAllowedException(String msg) {
        super(msg);
    }
}
