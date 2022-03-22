package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Participant;

 
@Repository
public interface ParticipantRepository 
        extends JpaRepository<Participant, Long> {
	
	Long deleteByName(String name);
	
 
}
