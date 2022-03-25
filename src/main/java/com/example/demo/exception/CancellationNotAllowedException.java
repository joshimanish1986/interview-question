package com.example.demo.exception;

public class CancellationNotAllowedException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CancellationNotAllowedException(String msg) {
        super(msg);
    }
}
