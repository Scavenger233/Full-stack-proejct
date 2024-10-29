package com.bytecaptain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bytecaptain.exception.CourseNotFoundException;
import com.bytecaptain.model.Course;
import com.bytecaptain.repository.CourseRepository;
import com.bytecaptain.service.impl.CourseServiceImpl;

@ExtendWith(SpringExtension.class)
public class CourseServiceMockTest {
	
	@Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService = new CourseServiceImpl();

    @Test
    public void getAllCourses() {
    	List<Course> courses = Arrays.asList(
                new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development."),
                new Course(10002, "GoneWithTheWind", "Tomorrow is another day!"));
    	
    	when(courseRepository.findAll()).thenReturn(courses);
		assertEquals(courses, courseService.getAllCourses("in28minutes"));
    }
    
    @Test
    public void getCourse() {
    	Course course = new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development.");
    	
    	when(courseRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(course));
		assertEquals(course, courseService.getCourse("JavaCourses", Long.valueOf(10001)));
    }
    
    @Test
    public void getCourseNotFound() {
    	
    	CourseNotFoundException exception = assertThrows(
    			CourseNotFoundException.class,
    	           () -> courseService.getCourse("JavaCourses", Long.valueOf(10001)),
    	           "Course id not found : 10001"
    	    );

    	    assertEquals("Course id not found : 10001", exception.getMessage());
    }
    
    @Test
    public void deleteCourse() {
    	
    	Course course = new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development.");
    	
    	when(courseRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(course));
    	courseService.deleteCourse("JavaCourses", Long.valueOf(10001));

		verify(courseRepository, times(1)).deleteById(Long.valueOf(10001));
    }
    
    @Test
    public void updateCourse() {
    	Course course = new Course(10001, "in28minutes", "An in-depth guide to Spring Boot development.");
    	
    	when(courseRepository.save(course)).thenReturn(course);
		assertEquals(course, courseService.updateCourse("JavaCourses", Long.valueOf(10001), course));
    }
    
    @Test
    public void createCourse() {
    	Course course = new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development.");
    	
    	when(courseRepository.save(course)).thenReturn(course);
		assertEquals(course, courseService.createCourse("JavaCourses", course));

    }

}
