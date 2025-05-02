package com.openclassrooms.safetyNet.model.view;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lecture seule
 */
@Entity
@Immutable
@Table(name = "person_with_medical_record_view") 
@Data
@NoArgsConstructor
public class PersonMedicalRecordView {

	@Id
    private Long personId;
	
    private String firstName;

    private String lastName;

    private String birthdate;

    @Column(columnDefinition = "TEXT")
    private String medications;

    @Column(columnDefinition = "TEXT")
    private String allergies;
}