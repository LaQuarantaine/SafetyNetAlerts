package com.openclassrooms.safetyNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetyNet.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> { 	//<entité manipulée, type de la clé primaire>
	
	// grâce à Sring Data, les méthodes suivantes seront automatiquement générées
	// comme par exemple : SELECT phone FROM person WHERE address IN (...)
	List <Person> findByAddressIn(List<String> addresses);
	List <Person> findByAddress(String address);
	List <String> findPhoneByAddressIn(List<String> addresses);
	List <Person> findByLastName(String lastName); 
}
