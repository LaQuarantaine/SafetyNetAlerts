package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.model.Person;

import lombok.RequiredArgsConstructor;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.PersonDTO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final JsonDataStore dataStore;
    private final JsonFileService jsonFileService;

    public PersonDTO addPerson(PersonDTO personDTO) {
        Person person = addPersonDTO(personDTO);
        dataStore.getPersons().add(person);
        jsonFileService.saveDataToFile();

        return personDTO;
    }

    public Optional<PersonDTO> updatePerson(String firstName, String lastName, PersonDTO personDTO) {
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
            
            return personDTO;

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

    private Person addPersonDTO(PersonDTO dto) {
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