package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.CourseEntity;
import com.example.demo.entity.Participant;
import com.example.demo.exception.RecordNotFoundException;

public interface CourseService {

	//List<CourseEntity> getAllCourses();

	CourseEntity getCourseById(Long id) throws RecordNotFoundException;

	List<CourseEntity> getCoursesByTitle(String title) throws RecordNotFoundException;

	CourseEntity addCourse(CourseEntity entity) throws Exception;

	CourseEntity addParticipant(Long courseId, Participant participant) throws Exception;

	CourseEntity removeParticipant(Long courseId, Participant participant) throws Exception;

}