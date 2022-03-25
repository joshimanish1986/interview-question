package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Course;
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
	
	/**
	 * Method returns courses for the id passed
	 * @param id
	 * @return Course
	 */
	@Override
	public Course getCourseById(Long id) throws RecordNotFoundException {
		Optional<Course> course = courseRepo.findById(id);

		if (course.isPresent()) {
			return course.get();
		} else {
			throw new RecordNotFoundException("No course record exist for given id");
		}
	}
	
	/**
	 * Method returns List of courses matching the title passed
	 * @param title
	 * @return Course
	 */
	@Override
	public List<Course> getCoursesByTitle(String title) throws RecordNotFoundException {
		List<Course> course = courseRepo.findByTitleIgnoreCase(title);

		if (null != course && !course.isEmpty()) {
			return course;
		} else {
			throw new RecordNotFoundException("No course record exist for given title");
		}
	}

	/**
	 * Creates new course and adds it to the Course Table
	 * @param Course entity
	 * @return Course
	 */
	@Override
	public Course addCourse(Course entity) throws Exception {
		entity.setRemaining(entity.getCapacity());
		entity = courseRepo.save(entity);

		return entity;
	}

	/**
	 * Adds a new participant for the given course Id based on different conditions.
	 * @param Participant participant
	 * Long courseId
	 * @return Course
	 */
	@Override
	public Course addParticipant(Long courseId, Participant participant) throws CourseIsFullException,
			RegistrationNotAllowedException, RecordNotFoundException, NameAlreadyEnrolledException, Exception {

		Course cEntity = courseRepo.findById(courseId).map(c -> {
			if (!c.getPartcipant().isEmpty()) {

				try {
					if (!participantAlreadyExists(c, participant) && isRegistrationAllowed(c, participant)
							&& isRegistrationAvailable(c)) {
						participant.setCourse(c);
						participantRepo.save(participant);
						c.getPartcipant().add(participant);
						c.setRemaining(c.getRemaining() - 1);
						
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
				c.getPartcipant().add(participant);
				c.setRemaining(c.getRemaining() - 1);
			}

			return c;
		}).orElseThrow(() -> new RecordNotFoundException("Course not found for the id  = " + courseId));

		return courseRepo.save(cEntity);
	}

	/**
	 * Removes the participant for the given course Id based on different conditions.
	 * @param Participant participant
	 * Long courseId
	 * @return Course
	 */
	@Override
	public Course removeParticipant(Long courseId, Participant participant)
			throws CancellationNotAllowedException, ParticipantNotFoundException, Exception {

		Course cEntity1 = null;
		try {
		cEntity1 = courseRepo.findById(courseId).get();
		}
		catch( NoSuchElementException e) {
			throw new RecordNotFoundException("Record not found for the course id  = " + courseId);
		}

		if (null != cEntity1) {

			if (!cEntity1.getPartcipant().isEmpty()) {
					if (null != participantExists(cEntity1, participant) && isCancellationAllowed(cEntity1, participant)) {

						participantRepo.deleteByName(participantExists(cEntity1, participant).get().getName());
						cEntity1.getPartcipant().removeIf(p -> p.getName().equalsIgnoreCase(participant.getName()));
						cEntity1.setRemaining(cEntity1.getRemaining() + 1);

					}

					
			}

			else {
				throw new ParticipantNotFoundException(
						"Participant doesnt exist for the given course ID " + cEntity1.getId());
			}

		} 
		return courseRepo.save(cEntity1);
	}

	/**
	 * @param course
	 * @param p
	 * @return
	 * @throws RegistrationNotAllowedException
	 */
	public boolean isRegistrationAllowed(Course course, Participant p) throws RegistrationNotAllowedException {

		long diffInMillies = Math.abs(p.getRegistrationDate().getTime() - course.getStartDate().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (p.getRegistrationDate().after(course.getStartDate()) || diff <= 3) {

			throw new RegistrationNotAllowedException(
					"Registration deadline over for this course Id " + course.getId());
		}

		return true;

	}

	/**
	 * @param course
	 * @return
	 * @throws CourseIsFullException
	 */
	public boolean isRegistrationAvailable(Course course) throws CourseIsFullException {

		if (course.getRemaining() <= 0) {

			throw new CourseIsFullException("Course is full for courseId " + course.getId());
		}

		return true;
	}

	/**
	 * @param course
	 * @param participant
	 * @return
	 * @throws NameAlreadyEnrolledException
	 */
	public boolean participantAlreadyExists(Course course, Participant participant)
			throws NameAlreadyEnrolledException {

			if(course.getPartcipant().stream().anyMatch(p -> p.getName().equalsIgnoreCase(participant.getName()))) {
				throw new NameAlreadyEnrolledException("Participant name already exists for courseId " + course.getId());
			
			}

		return false;
	}

	/**
	 * @param course
	 * @param participant
	 * @return
	 * @throws ParticipantNotFoundException
	 */
	public Optional<Participant> participantExists(Course course, Participant participant)
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

	/**
	 * @param course
	 * @param p
	 * @return
	 * @throws CancellationNotAllowedException
	 */
	public boolean isCancellationAllowed(Course course, Participant p) throws CancellationNotAllowedException {

		long diffInMillies = Math.abs(p.getCancelDate().getTime() - course.getStartDate().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (p.getCancelDate().after(course.getStartDate()) || diff <= 3) {

			throw new CancellationNotAllowedException(
					"Cancellation deadline over for this course Id " + course.getId());
		}

		return true;

	}
}
