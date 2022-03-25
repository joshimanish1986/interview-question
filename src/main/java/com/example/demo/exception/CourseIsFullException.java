package com.example.demo.exception;

public class CourseIsFullException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourseIsFullException(String msg) {
        super(msg);
    }
}
