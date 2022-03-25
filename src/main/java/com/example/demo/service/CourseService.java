package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Course;
import com.example.demo.entity.Participant;
import com.example.demo.exception.RecordNotFoundException;

public interface CourseService {

	//List<Course> getAllCourses();

	Course getCourseById(Long id) throws RecordNotFoundException;

	List<Course> getCoursesByTitle(String title) throws RecordNotFoundException;

	Course addCourse(Course entity) throws Exception;

	Course addParticipant(Long courseId, Participant participant) throws Exception;

	Course removeParticipant(Long courseId, Participant participant) throws Exception;

}