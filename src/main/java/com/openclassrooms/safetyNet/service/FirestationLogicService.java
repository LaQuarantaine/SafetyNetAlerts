package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.FirestationDTO;
import com.openclassrooms.safetyNet.dto.FirestationResponseDTO;
import com.openclassrooms.safetyNet.dto.PersonCoveredDTO;
import com.openclassrooms.safetyNet.model.Firestation;
import com.openclassrooms.safetyNet.model.MedicalRecord;
import com.openclassrooms.safetyNet.model.Person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirestationLogicService {

    private final FirestationService firestationService;
    private final PersonAgeService personAgeService;
    private final PersonAccessService personAccessService;
    private final MedicalRecordService medicalRecordService;
    private final JsonDataStore dataStore;

    	// utilisée par endpoint 1
    public FirestationResponseDTO getPersonsByStation(String stationNumber) {
        log.info("Recherche des personnes couvertes par la caserne n°{}", stationNumber);

        Set<String> addresses = firestationService.getAddressesForStation(stationNumber);

        List<Person> persons = personAccessService.getResidentsAtAddresses(addresses);
        log.debug("{} personne(s) trouvée(s) à ces adresses", persons.size());
        
        List<PersonCoveredDTO> result = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        for (Person person : persons) {
        	Integer age = personAgeService.getAgeForPerson(person);
        	if (age == null) {
        	    log.warn("Aucun dossier médical trouvé pour {} {}", person.getFirstName(), person.getLastName());
        	    continue;
        	}

            if (personAgeService.isChild(person)) childCount++;
            else adultCount++;

            result.add(new PersonCoveredDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    person.getPhone()
            ));
        }

        log.info("Résultat : {} adultes, {} enfants couverts", adultCount, childCount);
        return new FirestationResponseDTO(result, adultCount, childCount);
    }

    // utilisée par endpoint 3
    public List<String> getPhoneNumbersByStation(String stationNumber) {
    	Set<String> addresses = firestationService.getAddressesForStation(stationNumber);
    	
    	List<Person> persons = personAccessService.getResidentsAtAddresses(addresses);
    	
    	return persons.stream()
                .map(Person::getPhone)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public FireResponseDTO getFireAlert(String address) {
        // 1. Récupération du numéro de la caserne (le premier trouvé)
        String stationNumber = firestationService.getStationNumbersByAddress(address).stream()
                .findFirst()
                .orElse("N/A");

        // 2. Récupération des personnes habitant à cette adresse
        List<Person> persons = personAccessService.getResidentsAtAddress(address);

        // 3. Création des FireResidentDTO
        List<FireResidentDTO> residents = persons.stream()
                .map(person -> {
                	// Récupération du dossier médical par prénom + nom
                    Optional<MedicalRecord> medicalRecord = medicalRecordService
                            .getByName(person.getFirstName(), person.getLastName());

                    List<String> medications = medicalRecord
                            .map(MedicalRecord::getMedications)
                            .orElse(List.of());

                    List<String> allergies = medicalRecord
                            .map(MedicalRecord::getAllergies)
                            .orElse(List.of());


                    return new FireResidentDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getPhone(),
                            medications,
                            allergies
                    );
                })
                .collect(Collectors.toList());

        return new FireResponseDTO(stationNumber, residents);
    } 
    
    public Map<String, List<FireResidentDTO>> getFloodInfoByStations(List<String> stationNumbers) {
        Map<String, List<FireResidentDTO>> result = new HashMap<>();

        // 1. Récupération de toutes les adresses couvertes
        Set<String> addresses = stationNumbers.stream()
                .flatMap(station -> firestationService.getAddressesForStation(station).stream())
                .collect(Collectors.toSet());

        // 2. Pour chaque adresse
        for (String address : addresses) {
            List<Person> residents = personAccessService.getResidentsAtAddress(address);

            List<FireResidentDTO> residentDTOs = residents.stream()
                    .map(person -> {
                        Optional<MedicalRecord> medicalRecord = medicalRecordService
                                .getByName(person.getFirstName(), person.getLastName());

                        List<String> meds = medicalRecord.map(MedicalRecord::getMedications).orElse(List.of());
                        List<String> allergies = medicalRecord.map(MedicalRecord::getAllergies).orElse(List.of());

                        return new FireResidentDTO(
                                person.getFirstName(),
                                person.getLastName(),
                                person.getPhone(),
                                meds,
                                allergies
                        );
                    })
                    .collect(Collectors.toList());

            result.put(address, residentDTOs);
        }

        return result;
    }

    // Supprimer par adresse
    public boolean deleteFirestationByAddress(String address) {
        List<Firestation> firestations = dataStore.getFirestations().stream()
                .filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toList());

        if (firestations.isEmpty()) return false;

        firestationService.deleteByAddress(address);
        return true;
    }

 	// Supprimer par station
    public boolean deleteFirestationByStation(String station) {
        Set<String> before = firestationService.getAddressesForStation(station);
        firestationService.deleteByStation(station);
        return !before.isEmpty();
    }

 	// Mettre à jour un numéro de caserne pour une adresse
    public boolean updateFirestationMapping(String address, String newStation) {
        try {
            firestationService.updateStationNumber(address, newStation);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
 	
 	// Ajouter un nouveau mapping adresse/caserne
    public void addFirestationMapping(FirestationDTO firestationDTO) {
        Firestation firestation = new Firestation();
        firestation.setAddress(firestationDTO.getAddress());
        firestation.setStation(firestationDTO.getStation());
        firestationService.save(firestation);
    }
 
}
