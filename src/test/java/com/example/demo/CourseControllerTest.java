package com.example.demo;

import static org.junit.Assert.assertSame;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.demo.entity.CourseEntity;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.ICourseService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

	@TestConfiguration
	static class CourseServiceImplTestContextConfiguration {

		@Bean
		public CourseService courseService() {
			return new ICourseService();
		}
	}

	@Autowired
	private CourseService courseService;

	@MockBean
	private CourseRepository courseRepository;


	public void addCourse() throws Exception {
		final String pattern = "yyyy-MM-dd";
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		final Date startDate = simpleDateFormat.parse("2022-05-22");

		CourseEntity newCourse = new CourseEntity( "title", startDate, startDate, 10);
		CourseEntity course = courseService.addCourse(newCourse);

		assertSame(course.getTitle(), "title");

	}

}
