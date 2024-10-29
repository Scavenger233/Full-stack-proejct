package com.bytecaptain.exception;

public class CourseNotFoundException extends RuntimeException {
  
	private static final long serialVersionUID = 179856888745840942L;

	public CourseNotFoundException(Long id) {
        super("Course id not found : " + id);
    }
	
	public CourseNotFoundException(String message, Long id) {
        super("Course id not found : " + id + " TODO " + message);
    }
}
