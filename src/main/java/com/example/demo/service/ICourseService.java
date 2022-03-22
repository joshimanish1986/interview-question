package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CourseEntity;
import com.example.demo.entity.Participant;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.exception.NameAlreadyEnrolledException;
import com.example.demo.exception.RegistrationClosedException;
import com.example.demo.exception.CourseIsFullException;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.ParticipantRepository;

import lombok.Lombok;

@Service
@Transactional
public class ICourseService implements CourseService {

	@Autowired
	CourseRepository courseRepo;
	@Autowired
	ParticipantRepository participantRepo;

	@Override
	public CourseEntity getCourseById(Long id) throws RecordNotFoundException {
		Optional<CourseEntity> course = courseRepo.findById(id);

		if (course.isPresent()) {
			return course.get();
		} else {
			throw new RecordNotFoundException("No course record exist for given id");
		}
	}

	@Override
	public List<CourseEntity> getCoursesByTitle(String title) throws RecordNotFoundException {
		List<CourseEntity> course = courseRepo.findByTitleContainingIgnoreCase(title);

		if (null != course && !course.isEmpty()) {
			return course;
		} else {
			throw new RecordNotFoundException("No course record exist for given title");
		}
	}

	@Override
	public CourseEntity createCourse(CourseEntity entity) throws Exception {

		entity = courseRepo.save(entity);

		return entity;
	}

	@Override
	public CourseEntity addParticipant(Long courseId, Participant participant) throws CourseIsFullException,
			RegistrationClosedException, RecordNotFoundException, NameAlreadyEnrolledException, Exception {

		courseRepo.findById(courseId).map(c -> {
			if (!c.getPartcipant().isEmpty()) {

				System.out.println("Inside the participant is not empty");

				try {
					if (!participantAlreadyExists(c, participant) && isRegistrationAllowed(c, participant)
							&& isRegistrationAvailable(c)) {
						participant.setCourse(c);
						participantRepo.save(participant);
						c.setRemaining(c.getRemaining() - 1);
						courseRepo.save(c);
					}

					else {
						System.out.println("Record cant be created");
					}
				} catch (NameAlreadyEnrolledException e) {
					throw Lombok.sneakyThrow(e);
				} catch (RegistrationClosedException e) {
					throw Lombok.sneakyThrow(e);
				} catch (CourseIsFullException e) {
					throw Lombok.sneakyThrow(e);
				}

			} else {
				participant.setCourse(c);
				participantRepo.save(participant);
				c.setRemaining(c.getRemaining() - 1);
				courseRepo.save(c);
			}

			return c;
		}).orElseThrow(() -> new RecordNotFoundException("Course not found for the id  = " + courseId));

		return null;
	}

	@Override
	public CourseEntity removeParticipant(Long courseId, Participant participant) throws Exception {
		courseRepo.findById(courseId).map(c -> {
			if (!c.getPartcipant().isEmpty()) {
				try {
					if (null != participantExists(c, participant)
							&& isRegistrationCancellationAllowed(c, participant)) {

						participantRepo.deleteByName(participantExists(c, participant).get().getName());
						c.setRemaining(c.getRemaining() + 1);
						courseRepo.save(c);
					}

					else {
						System.out.println("participant not removed");
					}
				}

				catch (RecordNotFoundException e) {
					System.out.println("Conditions not met " + e.getMessage());
				}

			}

			return c;
		}).orElseThrow(() -> new RecordNotFoundException("Course not found for the id  = " + courseId));

		return null;
	}

	public boolean isRegistrationAllowed(CourseEntity course, Participant p) throws RegistrationClosedException {

		long diffInMillies = Math.abs(p.getRegistrationDate().getTime() - course.getStartDate().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (p.getRegistrationDate().after(course.getStartDate()) || diff <= 3) {

			throw new RegistrationClosedException("Registration deadline over for this course Id " + course.getId());
		}

		return true;

	}

	public boolean isRegistrationAvailable(CourseEntity course) throws CourseIsFullException {

		if (course.getRemaining() <= 0) {

			throw new CourseIsFullException("Course is full for courseId " + course.getId());
		}

		return true;
	}

	public boolean participantAlreadyExists(CourseEntity course, Participant participant)
			throws NameAlreadyEnrolledException {

		if (!course.getPartcipant().stream().filter(p -> p.getName().equalsIgnoreCase(participant.getName()))
				.findFirst().isEmpty()) {

			throw new NameAlreadyEnrolledException("Participant name already exists for courseId " + course.getId());
		}

		return false;
	}

	public Optional<Participant> participantExists(CourseEntity course, Participant participant)
			throws RecordNotFoundException {

		/*
		 * if (!course.getPartcipant().stream().filter(p ->
		 * p.getName().equalsIgnoreCase(participant.getName())) .findFirst().isEmpty())
		 * {
		 */

		Optional<Participant> user = course.getPartcipant().stream()
				.filter(p -> p.getName().equalsIgnoreCase(participant.getName())).findFirst();

		if (user != null && !user.isEmpty()) {

			return user;
		}

		/*
		 * return true; }
		 */

		return null;
	}

	public boolean isRegistrationCancellationAllowed(CourseEntity course, Participant p)
			throws RecordNotFoundException {

		long diffInMillies = Math.abs(p.getCancelDate().getTime() - course.getStartDate().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (p.getCancelDate().after(course.getStartDate()) || diff <= 3) {

			throw new RecordNotFoundException("Cancellation deadline over for this course Id " + course.getId());
		}

		return true;

	}
}
