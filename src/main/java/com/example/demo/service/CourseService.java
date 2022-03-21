package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CourseEntity;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.repository.CourseRepository;

@Service
public class CourseService {

	@Autowired
	CourseRepository repository;

	public List<CourseEntity> getAllCourses() {
		List<CourseEntity> courseList = repository.findAll();

		if (courseList.size() > 0) {
			return courseList;
		} else {
			return new ArrayList<CourseEntity>();
		}
	}

	public CourseEntity getCourseById(Long id) throws RecordNotFoundException {
		Optional<CourseEntity> course = repository.findById(id);

		if (course.isPresent()) {
			return course.get();
		} else {
			throw new RecordNotFoundException("No course record exist for given id");
		}
	}
	
	
	public List <CourseEntity> getCoursesByTitle(String title) throws RecordNotFoundException {
		List <CourseEntity> course = repository.findByTitleContainingIgnoreCase(title);

		if (null!= course && !course.isEmpty() ) {
			return course;
		} else {
			throw new RecordNotFoundException("No course record exist for given title");
		}
	}


	public CourseEntity createCourse(CourseEntity entity) throws Exception {

			entity = repository.save(entity);

			return entity;
	}

	public void deleteCourseById(Long id) throws RecordNotFoundException {
		Optional<CourseEntity> course = repository.findById(id);

		if (course.isPresent()) {
			repository.deleteById(id);
		} else {
			throw new RecordNotFoundException("No course record exist for given id");
		}
	}
}
