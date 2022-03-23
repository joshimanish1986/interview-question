package com.example.demo.service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CourseEntity;
import com.example.demo.entity.Participant;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.exception.NameAlreadyEnrolledException;
import com.example.demo.exception.ParticipantNotFoundException;
import com.example.demo.exception.RegistrationNotAllowedException;
import com.example.demo.exception.CancellationNotAllowedException;
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
	public CourseEntity addCourse(CourseEntity entity) throws Exception {
		entity.setRemaining(entity.getCapacity());		
		entity = courseRepo.save(entity);

		return entity;
	}

	@Override
	public CourseEntity addParticipant(Long courseId, Participant participant) throws CourseIsFullException,
			RegistrationNotAllowedException, RecordNotFoundException, NameAlreadyEnrolledException, Exception {

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

				} catch (NameAlreadyEnrolledException e) {
					throw Lombok.sneakyThrow(e);
				} catch (RegistrationNotAllowedException e) {
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

		return courseRepo.findById(courseId).get();
	}

	@Override
	public CourseEntity removeParticipant(Long courseId, Participant participant)
			throws CancellationNotAllowedException, ParticipantNotFoundException, Exception {
		courseRepo.findById(courseId).map(c -> {
			if (!c.getPartcipant().isEmpty()) {
				try {
					if (null != participantExists(c, participant) && isCancellationAllowed(c, participant)) {

						participantRepo.deleteByName(participantExists(c, participant).get().getName());
						c.setRemaining(c.getRemaining() + 1);
						courseRepo.save(c);
					}

					else {
						System.out.println("participant not removed");
					}
				}

				catch (ParticipantNotFoundException e) {
					throw Lombok.sneakyThrow(e);
				} catch (CancellationNotAllowedException e) {
					throw Lombok.sneakyThrow(e);
				}

			}

			return c;
		}).orElseThrow(() -> new RecordNotFoundException("Course not found for the id  = " + courseId));

		return courseRepo.findById(courseId).get();
	}

	public boolean isRegistrationAllowed(CourseEntity course, Participant p) throws RegistrationNotAllowedException {

		long diffInMillies = Math.abs(p.getRegistrationDate().getTime() - course.getStartDate().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (p.getRegistrationDate().after(course.getStartDate()) || diff <= 3) {

			throw new RegistrationNotAllowedException(
					"Registration deadline over for this course Id " + course.getId());
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
			throws ParticipantNotFoundException {

		Optional<Participant> user = course.getPartcipant().stream()
				.filter(p -> p.getName().equalsIgnoreCase(participant.getName())).findFirst();

		if (user != null && !user.isEmpty()) {

			return user;
		}

		else {
			throw new ParticipantNotFoundException(
					"Participant doesnt exist for the given course ID " + course.getId());
		}

	}

	public boolean isCancellationAllowed(CourseEntity course, Participant p) throws CancellationNotAllowedException {

		long diffInMillies = Math.abs(p.getCancelDate().getTime() - course.getStartDate().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (p.getCancelDate().after(course.getStartDate()) || diff <= 3) {

			throw new CancellationNotAllowedException(
					"Cancellation deadline over for this course Id " + course.getId());
		}

		return true;

	}
}
