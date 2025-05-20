package com.openclassrooms.safetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.PersonCreateDTO;
import com.openclassrooms.safetyNet.dto.PersonUpdateDTO;
import com.openclassrooms.safetyNet.model.Person;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@Mock
    private JsonDataStore dataStore;

    @Mock
    private JsonFileService jsonFileService;

    @InjectMocks
    private PersonService personService;

    private List<Person> persons;
    private Person marie;

    @BeforeEach
    void setup() {
        marie = new Person("Marie", "Durant", "12 rue des Alpes", "Paris", "75000", "0101010101", "marie@gmail.com");
        persons = new ArrayList<>(List.of(marie));

        when(dataStore.getPersons()).thenReturn(persons);
    }
    
    
    @Test
    @DisplayName("Ajoute une nouvelle personne et sauvegarde les données")
    void addPerson_shouldAddPersonAndPersist() {
        PersonCreateDTO newPerson = new PersonCreateDTO("Carole", "Nouple", "4 rue Jolimont", "Lyon", "69000", "0202020202", "carole@gmail.com");

        PersonCreateDTO result = personService.addPerson(newPerson);

        assertEquals(newPerson, result);
        assertTrue(persons.stream().anyMatch(p -> p.getFirstName().equals("Carole") && p.getLastName().equals("Nouple")));
        verify(jsonFileService).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("Met à jour une personne existante et sauvegarde les données")
    void updatePerson_shouldUpdateAndPersistIfFound() {
    	PersonUpdateDTO updateDTO = new PersonUpdateDTO("4 rue de la Source", "Lyon", "69000", "0101010101", "marie@gmail.com");

        Optional<PersonUpdateDTO> result = personService.updatePerson("Marie", "Durant", updateDTO);

        assertTrue(result.isPresent());
        assertEquals("4 rue de la Source", marie.getAddress());
        assertEquals("Lyon", marie.getCity());
        assertEquals("69000", marie.getZip());
        assertEquals("0101010101", marie.getPhone());
        assertEquals("marie@gmail.com", marie.getEmail());

        verify(jsonFileService).saveDataToFile();
    }
    
    @Test
    @DisplayName("Ne met pas à jour si la personne est introuvable")
    void updatePerson_shouldReturnEmptyIfNotFound() {
    	PersonUpdateDTO updateDTO = new PersonUpdateDTO ("adresse", "Paris", "75000", "9999999999", "inconnu@gmail.com");

        Optional<PersonUpdateDTO> result = personService.updatePerson("Personne", "Inconnue", updateDTO);

        assertTrue(result.isEmpty());
        verify(jsonFileService, never()).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("Supprime une personne existante et sauvegarde les données")
    void deletePerson_shouldRemoveAndPersistIfFound() {
        boolean result = personService.deletePerson("Marie", "Durant");

        assertTrue(result);
        assertTrue(persons.isEmpty());
        verify(jsonFileService).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("Ne supprime rien si la personne est introuvable")
    void deletePerson_shouldReturnFalseIfNotFound() {
        boolean result = personService.deletePerson("Peronne", "Inconnue");

        assertFalse(result);
        verify(jsonFileService, never()).saveDataToFile();
    }	
}
