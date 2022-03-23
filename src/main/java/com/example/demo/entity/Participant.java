package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "PARTICIPANT")
@Getter
@Setter
public class Participant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "courseId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private CourseEntity course;

	@Column(name = "name")
	private String name;

	@Column(name = "registration_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date registrationDate;
	
	@Column(name = "cancellation_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date cancelDate;

	@Override
	public String toString() {
		return "Participant [id=" + id + ", course=" + course + ", name=" + name + ", registrationDate="
				+ registrationDate + ", cancelDate=" + cancelDate + "]";
	}

	

}
