package com.openclassrooms.safetyNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetyNet.model.view.PersonMedicalRecordView;

@Repository
public interface PersonMedicalRecordRepository extends JpaRepository<PersonMedicalRecordView, Long> {
	
	List<PersonMedicalRecordView> findByFirstNameAndLastName(String firstName, String lastName);
}
