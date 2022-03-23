package com.example.demo.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.CourseEntity;
import com.example.demo.entity.Participant;
import com.example.demo.exception.CancellationNotAllowedException;
import com.example.demo.exception.CourseIsFullException;
import com.example.demo.exception.NameAlreadyEnrolledException;
import com.example.demo.exception.ParticipantNotFoundException;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.exception.RegistrationNotAllowedException;
import com.example.demo.service.ICourseService;



@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	ICourseService courseService;

	@PostMapping(path = "", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CourseEntity> addCourse(@RequestBody CourseEntity course)

	{
		try {
			courseService.addCourse(course);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<CourseEntity>(course, HttpStatus.CREATED);
	}

	@GetMapping(path = "", produces = "application/json")
	public ResponseEntity<CourseEntity> getCourseByTitle(@RequestParam("q") String title) {

		List<CourseEntity> course = null;
		try {
			course = courseService.getCoursesByTitle(title);
		} catch (RecordNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(course, HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<CourseEntity> getCourseById(@PathVariable("id") Long id) {
		CourseEntity course;
		try {
			course = courseService.getCourseById(id);
		} catch (RecordNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(course, HttpStatus.OK );
	}

	@PostMapping(path = "/{courseId}/add", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CourseEntity> addParticipant(@PathVariable(value = "courseId") Long courseId,
			@RequestBody Participant participant)

	{
		CourseEntity course = null;
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

		return new ResponseEntity<CourseEntity>(course, HttpStatus.OK);
	}

	@DeleteMapping(path = "/{courseId}/remove", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CourseEntity> cancelEnrollment(@PathVariable(value = "courseId") Long courseId,
			@RequestBody Participant participant)

	{
		CourseEntity course = null;
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
		return new ResponseEntity<CourseEntity>(course, HttpStatus.CREATED);
	}

}
