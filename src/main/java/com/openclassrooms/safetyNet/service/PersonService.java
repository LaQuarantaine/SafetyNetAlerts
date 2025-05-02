package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.dto.PersonDTO;
import com.openclassrooms.safetyNet.model.entity.Person;
import com.openclassrooms.safetyNet.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person addPerson(PersonDTO personDTO) {
        Person person = addPersonDTO(personDTO);
        return personRepository.save(person);
    }

    public Optional<Person> updatePerson(String firstName, String lastName, PersonDTO personDTO) {
        Optional<Person> existingPersonOpt = personRepository.findByFirstNameAndLastName(firstName, lastName);

        return existingPersonOpt.map(existingPerson -> {
            existingPerson.setAddress(personDTO.getAddress());
            existingPerson.setCity(personDTO.getCity());
            existingPerson.setZip(personDTO.getZip());
            existingPerson.setPhone(personDTO.getPhone());
            existingPerson.setEmail(personDTO.getEmail());
            return personRepository.save(existingPerson);
        });
    }

    public boolean deletePerson(String firstName, String lastName) {
        Optional<Person> personOpt = personRepository.findByFirstNameAndLastName(firstName, lastName);
        if (personOpt.isPresent()) {
            personRepository.delete(personOpt.get());
            return true;
        } else {
            return false;
        }
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