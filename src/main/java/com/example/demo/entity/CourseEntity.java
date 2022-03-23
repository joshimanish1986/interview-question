package com.example.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private Integer remaining ;
    
    @OneToMany(mappedBy="course")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Participant> partcipant;

	@Override
	public String toString() {
		return "CourseEntity [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", capacity=" + capacity + ", remaining=" + remaining + ", partcipant=" + partcipant + "]";
	}

	public CourseEntity( String title, Date startDate, Date endDate, Integer capacity) {
		
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.capacity = capacity;
	}


    
}
