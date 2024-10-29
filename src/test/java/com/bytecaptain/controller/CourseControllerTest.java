package com.bytecaptain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bytecaptain.model.Course;
import com.bytecaptain.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;
    
    private static final ObjectMapper om = new ObjectMapper();

    //TODO move to base class as sample data
    Course mockCourse = new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development.");

    String exampleCourseJson = "{\"id\":10001,\"username\":\"JavaCourses\",\"description\":\"An in-depth guide to Spring Boot development.\"}";

    @Test
    public void getCourse() throws Exception {

        Mockito.when(courseService.getCourse("JavaCourses",10001)).thenReturn(mockCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/library/JavaCourses/courses/10001").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(exampleCourseJson, result.getResponse().getContentAsString(), false);

    }

    @Test
    public void createCourse() throws Exception {

    	Course course = new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development.");

        Mockito.when(courseService.createCourse(Mockito.anyString(), Mockito.any(Course.class))).thenReturn(course);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/library/JavaCourses/courses").content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertEquals("http://localhost/library/JavaCourses/courses/10001",
                response.getHeader(HttpHeaders.LOCATION));

    }
    
    @Test
    public void updateCourse() throws Exception {

    	Course course = new Course(10001, "JavaCourses", "An in-depth guide to Spring Boot development.");

        Mockito.when(courseService.updateCourse(Mockito.anyString(), Mockito.anyLong(), Mockito.any(Course.class))).thenReturn(course);
        
        String courseString = om.writeValueAsString(course);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/library/JavaCourses/courses/10001")
                .contentType(MediaType.APPLICATION_JSON).content(courseString);
        

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        JSONAssert.assertEquals(exampleCourseJson, result.getResponse().getContentAsString(), false);

    }
    
    @Test
    public void deleteCourse() throws Exception {

    	doNothing().when(courseService).deleteCourse("JavaCourses", Long.valueOf(10001));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/library/JavaCourses/courses/10001");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

    }

}
