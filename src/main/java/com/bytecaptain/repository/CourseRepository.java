package com.bytecaptain.repository;
import java.util.List;


import com.bytecaptain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
	List<Course> findByUsername(String username);
}