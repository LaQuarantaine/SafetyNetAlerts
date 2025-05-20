package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.model.Person;


import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.PersonCreateDTO;
import com.openclassrooms.safetyNet.dto.PersonUpdateDTO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final JsonDataStore dataStore;
    private final JsonFileService jsonFileService;

    
    public PersonService(JsonDataStore dataStore, JsonFileService jsonFileService) {
		super();
		this.dataStore = dataStore;
		this.jsonFileService = jsonFileService;
	}

 
	public PersonCreateDTO addPerson(PersonCreateDTO personDTO) {
        Person person = addPersonDTO(personDTO);
        dataStore.getPersons().add(person);
        jsonFileService.saveDataToFile();

        return personDTO;
    }

	public Optional<PersonUpdateDTO> updatePerson(String firstName, String lastName, PersonUpdateDTO personDTO) {
	    Optional<Person> personOpt = dataStore.getPersons().stream()
	            .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
	            .findFirst();

	    return personOpt.map(existingPerson -> {
	        existingPerson.setAddress(personDTO.getAddress());
	        existingPerson.setCity(personDTO.getCity());
	        existingPerson.setZip(personDTO.getZip());
	        existingPerson.setPhone(personDTO.getPhone());
	        existingPerson.setEmail(personDTO.getEmail());

	        jsonFileService.saveDataToFile();

	        // Retourne un nouveau DTO basé sur la personne mise à jour
	        return new PersonUpdateDTO(
	            existingPerson.getAddress(),
	            existingPerson.getCity(),
	            existingPerson.getZip(),
	            existingPerson.getPhone(),
	            existingPerson.getEmail()
	        );
	    });
	}


    public boolean deletePerson(String firstName, String lastName) {
    	List<Person> persons = dataStore.getPersons();
        boolean removed = persons.removeIf(p ->
                p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));
        if (removed) {
            jsonFileService.saveDataToFile();
        }
        return removed;
    }

    private Person addPersonDTO(PersonCreateDTO dto) {
        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAddress(dto.getAddress());
        person.setCity(dto.getCity());
        person.setZip(dto.getZip());
        person.setPhone(dto.getPhone());
        person.setEmail(dto.getEmail());
        return person;
    }
}