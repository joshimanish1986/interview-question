package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
@Table(name="TBL_COURSES")
@Getter @Setter
public class CourseEntity {
 
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
     
    @Column(name="title")
    private String title;
     
    @Column(name="start_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    
    
    @Column(name="end_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    
    
    @Column(name="capacity")
    private Integer capacity;
    
    
    @Column(name ="remaining")
    private Integer remaining = 10;

    
    //Setters and getters
    

	@Override
	public String toString() {
		return "CourseEntity [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", capacity=" + capacity + ", remaining=" + remaining + "]";
	}
     
  
    
}
