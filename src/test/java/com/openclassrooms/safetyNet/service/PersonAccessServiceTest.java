package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonAccessServiceTest {

    @Mock
    private JsonDataStore dataStore;

    @InjectMocks
    private PersonAccessService personAccessService;

    private List<Person> persons;

    @BeforeEach
    void setup() {
        persons = Arrays.asList(
                new Person("Augustin", "Notini", "23 avenue du Roi", "Versailles", "78000", "0182477200", "alice@gmail.com"),
                new Person("Gerard", "Malbarre", "17 rue d'Italie", "Paris", "75010", "0987654321", "gerard@gmail.com"),
                new Person("Alice", "Notini", "23 avenue du Roi", "Versailles", "78000", "0182477200", "alice@gmail.com")
        );
    }

    
    @Test
    @DisplayName("Retourne les habitants d'une adresse donnée")
    void getResidentsAtAddress_shouldReturnMatchingPersons() {
        when(dataStore.getPersons()).thenReturn(persons);

        List<Person> result = personAccessService.getResidentsAtAddress("23 avenue du Roi");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Person::getFirstName, Person::getLastName)
                          .containsExactlyInAnyOrder(
                        		  tuple("Augustin", "Notini"),
                        		  tuple("Alice", "Notini")
                        		  );
    }

    
    @Test
    @DisplayName("Retourne les habitants des adresses données")
    void getResidentsAtAddresses_shouldReturnAllMatchingPersons() {
        when(dataStore.getPersons()).thenReturn(persons);

        List<Person> result = personAccessService.getResidentsAtAddresses(Set.of("23 avenue du Roi", "17 rue d'Italie"));

        assertThat(result).hasSize(3);
    }

    
    @Test
    @DisplayName("Retourne les habitants d'une ville donnée")
    void testGetResidentsAtCity_shouldReturnPersonsInCity() {
    	when(dataStore.getPersons()).thenReturn(persons); 
        List<Person> result = personAccessService.getResidentsAtCity("Versailles");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getCity().equals("Versailles")));
              		 
    }
}
