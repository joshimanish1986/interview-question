package com.example.demo.exception;

public class NameAlreadyEnrolledException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NameAlreadyEnrolledException(String msg) {
        super(msg);
    }
}
