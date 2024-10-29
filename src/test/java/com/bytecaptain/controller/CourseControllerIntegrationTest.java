package com.bytecaptain.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bytecaptain.BaseIntegrationTest;
import com.bytecaptain.SprintBootCrudApplication;
import com.bytecaptain.exception.CourseNotFoundException;
import com.bytecaptain.model.Course;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SprintBootCrudApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = OrderAnnotation.class)
@ActiveProfiles("test")
public class CourseControllerIntegrationTest extends BaseIntegrationTest {
    @LocalServerPort
    private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void addCourse() {

    	Course course = new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development.");

        HttpEntity<Course> entity = new HttpEntity<>(course, getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/instructors/JavaCourses/courses"),
                HttpMethod.POST, entity, String.class);

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertTrue(actual.contains("/instructors/JavaCourses/courses"));

    }
    
    @Test
    @Order(2)
    public void updateCourse() throws JSONException {

    	Course course = new Course(1, "JavaCourses", "An in-depth guide to Spring Boot development. updated");

        HttpEntity<Course> entity = new HttpEntity<>(course, getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/instructors/JavaCourses/courses/1"),
                HttpMethod.PUT, entity, String.class);
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

        String expected = "{\"id\":1,\"username\":\"JavaCourses\",\"description\":\"An in-depth guide to Spring Boot development. updated\"}";

        JSONAssert.assertEquals(expected, response.getBody(), false);

    }
    
    @Test
    @Order(3)
    public void testGetCourse() throws JSONException, JsonProcessingException {

        HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());

        ResponseEntity<String> response1 = restTemplate.exchange(
                createURLWithPort("/api/instructors/JavaCourses/courses/1"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"id\":1,\"username\":\"JavaCourses\",\"description\":\"An in-depth guide to Spring Boot development. updated\"}";

        JSONAssert.assertEquals(expected, response1.getBody(), false);
        
    }
    
	@Test
	@Order(4)
	public void testDeleteCourse() {
		Course course = restTemplate.getForObject(createURLWithPort("/api/instructors/JavaCourses/courses/1"), Course.class);
		assertNotNull(course);

		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/instructors/JavaCourses/courses/1"),
                HttpMethod.DELETE, entity, String.class);
		
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());

		try {
			course = restTemplate.getForObject("/api/instructors/JavaCourses/courses/1", Course.class);
		} catch (CourseNotFoundException e) {
			assertEquals("Course id not found : 1", e.getMessage());
		}
	}

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
