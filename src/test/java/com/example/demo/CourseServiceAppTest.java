package com.example.demo;

import java.nio.charset.Charset;
import java.util.Date;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.CourseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class CourseServiceAppTest {

	@Autowired
	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@org.junit.jupiter.api.Test
	public void testCourseService() throws Exception {

		CourseEntity reqObj = new CourseEntity();
		reqObj.setTitle("CT1");
		reqObj.setStartDate(new Date());
		reqObj.setEndDate(new Date());
		reqObj.setCapacity(10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(reqObj);

		this.mockMvc.perform(post("/courses").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().is2xxSuccessful());
		
		this.mockMvc.perform(get("/courses/1")).andDo(print()).andExpect(status().
				isOk()) .andExpect(content().string(containsString("CT1")));
		
		this.mockMvc.perform(get("/courses?q=CT1")).andDo(print()).andExpect(status().
				isOk()) .andExpect(content().string(containsString("CT1")));
		 
	}
	
	

}