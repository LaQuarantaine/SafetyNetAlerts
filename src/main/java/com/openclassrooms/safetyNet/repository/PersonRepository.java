package com.openclassrooms.safetyNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetyNet.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
