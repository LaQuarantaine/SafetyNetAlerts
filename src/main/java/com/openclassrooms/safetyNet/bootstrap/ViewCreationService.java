package com.openclassrooms.safetyNet.bootstrap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewCreationService {
	
	private final JdbcTemplate jdbcTemplate;
	
	public void createOrReplacePersonWithMedicalRecordView() {
		String sql = """
                CREATE OR REPLACE VIEW person_with_medical_record_view AS
                SELECT 
                	persons.id,
                    persons.first_name,
                    persons.last_name,
                    medicalrecords.birthdate,
                    medicalrecords.medications,
                    medicalrecords.allergies
                FROM persons
                JOIN medicalrecords
                ON persons.first_name = medicalrecords.first_name AND persons.last_name = medicalrecords.last_name;
                """;

        jdbcTemplate.execute(sql);
	}

	public void createOrReplaceStationAddressView() {
	    String sql = """
	        CREATE OR REPLACE VIEW station_address_view AS
	        SELECT 
	            firestations.station,
	            firestations.address,
	            persons.city,
	            persons.zip
	        FROM firestations
	        JOIN persons 
	        ON firestations.address = persons.address
	    """;
	    jdbcTemplate.execute(sql);
	}

	
}
