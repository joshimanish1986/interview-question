package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.Course;
import com.example.demo.entity.Participant;
import com.example.demo.exception.CancellationNotAllowedException;
import com.example.demo.exception.CourseIsFullException;
import com.example.demo.exception.NameAlreadyEnrolledException;
import com.example.demo.exception.ParticipantNotFoundException;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.exception.RegistrationNotAllowedException;
import com.example.demo.service.ICourseService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseController {

	@Autowired
	ICourseService courseService;

	@PostMapping(path = "/courses", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Course> addCourse(@RequestBody Course course)

	{
		log.info(" Inside addCourse API");
		try {

			courseService.addCourse(course);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Course>(course, HttpStatus.CREATED);
	}

	@GetMapping(path = "/courses", produces = "application/json")
	public ResponseEntity<Course> getCourseByTitle(@RequestParam("q") String title) {

		log.info(" Inside getCourseByTitle API for course title :: " + title);

		List<Course> course = null;
		try {
			course = courseService.getCoursesByTitle(title);
		} catch (RecordNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(course, HttpStatus.OK);

	}

	@GetMapping("/courses/{id}")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long id) {

		log.info(" Inside getCourseById API for course Id :: " + id);
		Course course = null;
		try {

			course = courseService.getCourseById(id);
		} catch (RecordNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(course, HttpStatus.OK);
	}

	@PostMapping(path = "/courses/{courseId}/add", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Course> addParticipant(@PathVariable(value = "courseId") Long courseId,
			@RequestBody Participant participant)

	{
		log.info(" Inside addParticipant API for course Id :: " + courseId);
		Course course = null;
		try {
			course = courseService.addParticipant(courseId, participant);
		} catch (NameAlreadyEnrolledException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (RegistrationNotAllowedException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (CourseIsFullException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (RecordNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}

	@PostMapping(path = "/courses/{courseId}/remove", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Course> removeParticipant(@PathVariable(value = "courseId") Long courseId,
			@RequestBody Participant participant)

	{
		log.info(" Inside removeParticipant API for course Id :: " + courseId);
		Course course = null;
		try {
			course = courseService.removeParticipant(courseId, participant);
		} catch (CancellationNotAllowedException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (ParticipantNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (RecordNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Course>(course, HttpStatus.CREATED);
	}

}
