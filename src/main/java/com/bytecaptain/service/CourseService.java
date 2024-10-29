package com.bytecaptain.service;

import java.util.List;

import com.bytecaptain.model.Course;

public interface CourseService {

	List<Course> getAllCourses(String username);
	
	Course getCourse(String username, long id);
	
	void deleteCourse(String username, long id);
	
	Course updateCourse(String username, long id, Course course);
	
	Course createCourse(String username, Course course);
}
